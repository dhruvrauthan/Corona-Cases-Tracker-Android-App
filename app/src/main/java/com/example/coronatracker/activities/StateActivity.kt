package com.example.coronatracker.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.coronatracker.CoronaService
import com.example.coronatracker.R
import com.example.coronatracker.fragments.SearchFragment
import com.example.coronatracker.models.ApiResponse
import com.example.coronatracker.models.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat

class StateActivity : AppCompatActivity() {

    //UI
    private lateinit var mDeathTextView: TextView
    private lateinit var mConfirmedTextView: TextView
    private lateinit var mRecoveredTextView: TextView
    private lateinit var mProgressBar: ProgressBar

    //Variables
    private lateinit var mCoronaService: CoronaService
    private var mPosition: Int = 0
    private lateinit var mStates: List<State>
    private lateinit var mStateCode:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        supportActionBar?.title="State Data"

        val intent = intent
        mPosition = intent.getIntExtra("pos", 0)

        initView()

        setStateCode()

        retrofitOperations()

        updateTextView()

    }

    private fun setStateCode() {

        when(mPosition){

            0-> mStateCode="AN"
            1-> mStateCode="AP"
            2-> mStateCode="AR"
            3-> mStateCode="AS"
            4-> mStateCode="BR"
            5-> mStateCode="CH"
            6-> mStateCode="CT"
            7-> mStateCode="DN"
            8-> mStateCode="DD"
            9-> mStateCode="DL"
            10-> mStateCode="GA"
            11-> mStateCode="GJ"
            12-> mStateCode="HR"
            13-> mStateCode="HP"
            14-> mStateCode="JK"
            15-> mStateCode="JH"
            16-> mStateCode="KA"
            17-> mStateCode="KL"
            18-> mStateCode="LA"
            19-> mStateCode="LD"
            20-> mStateCode="MP"
            21-> mStateCode="MH"
            22-> mStateCode="MN"
            23-> mStateCode="ML"
            24-> mStateCode="MZ"
            25-> mStateCode="NL"
            26-> mStateCode="OD"
            27-> mStateCode="PY"
            28-> mStateCode="PB"
            29-> mStateCode="RJ"
            30-> mStateCode="SK"
            31-> mStateCode="TN"
            32-> mStateCode="TG"
            33-> mStateCode="TR"
            34-> mStateCode="UP"
            35-> mStateCode="UT"
            36-> mStateCode="WB"

        }

    }

    private fun updateTextView() {

        mProgressBar.visibility = View.VISIBLE

        val call = mCoronaService.getResults()
        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {

                Log.v("tag", "failure")

            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                if (response.isSuccessful) {

                    Log.v("tag", "successful")

                    val apiResponse = response.body()
                    mStates = apiResponse?.states!!

                    getStateData()


                } else {

                    mProgressBar.visibility = View.GONE
                    Log.v("tag", "unsuccessful")

                }

            }


        })

    }

    private fun getStateData() {

        var deaths= 0
        var confirmed = 0
        var recovered = 0

        for (state in mStates) {

            if(state.statecode == mStateCode){

                deaths= state.deaths
                confirmed=state.confirmed
                recovered=state.recovered

            }

        }

        mDeathTextView.setText(
            "Deaths: " + DecimalFormat("##,##,###").format(
                deaths
            ).toString()
        )
        mConfirmedTextView.setText(
            "Confirmed: " + DecimalFormat("##,##,###").format(
                confirmed
            ).toString()
        )
        mRecoveredTextView.setText(
            "Recovered: " + DecimalFormat("##,##,###").format(
                recovered
            ).toString()
        )

        mProgressBar.visibility=View.GONE

    }

    private fun retrofitOperations() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.covid19india.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mCoronaService = retrofit.create(CoronaService::class.java)
    }

    private fun initView() {

        mDeathTextView = findViewById(R.id.state_deaths_textview)
        mConfirmedTextView = findViewById(R.id.state_confirmed_textview)
        mRecoveredTextView = findViewById(R.id.state_recovered_textview)
        mProgressBar = findViewById(R.id.state_progress_bar)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)

    }
}
