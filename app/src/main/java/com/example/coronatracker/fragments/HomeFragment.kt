package com.example.coronatracker.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.coronatracker.CoronaService
import com.example.coronatracker.R
import com.example.coronatracker.activities.MainActivity
import com.example.coronatracker.models.ApiResponse
import com.example.coronatracker.models.DataPerDay
import com.example.coronatracker.models.State
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat

class HomeFragment : Fragment() {

    private lateinit var root: View

    //UI
    private lateinit var mDeathTextView: TextView
    private lateinit var mConfirmedTextView: TextView
    private lateinit var mRecoveredTextView: TextView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mLineChart: LineChart

    //Variables
    private lateinit var mCoronaService: CoronaService
    private lateinit var mStates: List<State>
    private lateinit var mDataPerDays: List<DataPerDay>
    private val mTotalDeceasedTillNowList = ArrayList<Int>()
    private val mTotalConfirmedTillNowList = ArrayList<Int>()
    private val mTotalRecoveredTillNowList = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_home, container, false)

        (activity as MainActivity).supportActionBar?.title="Home"

        initView()

        retrofitOperations()

        updateTextView()

        return root
    }

    private fun initView() {

        mDeathTextView = root.findViewById(R.id.deaths_textview)
        mConfirmedTextView = root.findViewById(R.id.confirmed_textview)
        mRecoveredTextView = root.findViewById(R.id.recovered_textview)
        mProgressBar = root.findViewById(R.id.progress_bar)
        mLineChart=root.findViewById(R.id.line_chart)

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
                    mDataPerDays = apiResponse.dataPerDays

                    mProgressBar.visibility = View.GONE

                    mDeathTextView.setText(
                        "Deaths: " + DecimalFormat("##,##,###").format(
                            mStates?.get(0)?.deaths
                        ).toString()
                    )
                    mConfirmedTextView.setText(
                        "Confirmed:" + DecimalFormat("##,##,###").format(
                            mStates?.get(0)?.confirmed
                        ).toString()
                    )
                    mRecoveredTextView.setText(
                        "Recovered:" + DecimalFormat("##,##,###").format(
                            mStates?.get(0)?.recovered
                        ).toString()
                    )

                    setChartData()


                } else {

                    mProgressBar.visibility = View.GONE
                    Log.v("tag", "unsuccessful")

                }

            }


        })

    }

    private fun setChartData() {

        for (dataPerDay in mDataPerDays) {

            mTotalDeceasedTillNowList.add(dataPerDay.totalDeceasedTillNow)
            mTotalConfirmedTillNowList.add(dataPerDay.totalConfirmedTillNow)
            mTotalRecoveredTillNowList.add(dataPerDay.totalRecoveredTillNow)

        }

        val confirmedEntriesList = ArrayList<Entry>()
        val deceasedEntriesList = ArrayList<Entry>()
        val recoveredEntriesList= ArrayList<Entry>()
        val xAxis= ArrayList<Float>()

        for(i in 0 until mTotalConfirmedTillNowList.size){

            val confirmedEntry= Entry(i.toFloat(),mTotalConfirmedTillNowList[i].toFloat())
            val deceasedEntry= Entry(i.toFloat(),mTotalDeceasedTillNowList[i].toFloat())
            val recoveredEntry= Entry(i.toFloat(),mTotalRecoveredTillNowList[i].toFloat())

            xAxis.add(i.toFloat())

            confirmedEntriesList.add(confirmedEntry)
            deceasedEntriesList.add(deceasedEntry)
            recoveredEntriesList.add(recoveredEntry)

        }

        val confirmedDataSet= LineDataSet(confirmedEntriesList,"Confirmed Cases")
        val deceasedDataSet= LineDataSet(deceasedEntriesList,"Deaths")
        val recoveredDataSet= LineDataSet(recoveredEntriesList,"Recoveries")

        confirmedDataSet.setDrawValues(false)
        deceasedDataSet.setDrawValues(false)
        recoveredDataSet.setDrawValues(false)

        confirmedDataSet.color = Color.GRAY
        confirmedDataSet.setCircleColor(Color.GRAY)
        confirmedDataSet.setDrawCircles(false)

        deceasedDataSet.color = Color.RED
        deceasedDataSet.setCircleColor(Color.RED)
        deceasedDataSet.setDrawCircles(false)

        recoveredDataSet.color = Color.GREEN
        recoveredDataSet.setDrawCircles(false)
        recoveredDataSet.setCircleColor(Color.GREEN)

        val mLineData = LineData()

        mLineData.addDataSet(confirmedDataSet)
        mLineData.addDataSet(deceasedDataSet)
        mLineData.addDataSet(recoveredDataSet)

        mLineChart.description.text="Number of days"

        mLineChart.data= mLineData
        mLineChart.invalidate()

    }

    private fun retrofitOperations() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.covid19india.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mCoronaService = retrofit.create(CoronaService::class.java)

    }

}
