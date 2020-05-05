package com.example.coronatracker.fragments

import  android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coronatracker.CoronaService
import com.example.coronatracker.R
import com.example.coronatracker.activities.MainActivity
import com.example.coronatracker.models.ApiResponse
import com.example.coronatracker.models.DataPerDay
import com.example.coronatracker.models.State
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat

class HomeFragment : Fragment() {

    private lateinit var root: View

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

        retrofitOperations()

        updateTextView()

        return root
    }

    private fun updateTextView() {

        progress_bar?.visibility = View.VISIBLE

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

                    progress_bar.visibility = View.GONE

                    deaths_textview.setText(
                        "Deaths: " + DecimalFormat("##,##,###").format(
                            mStates?.get(0)?.deaths
                        ).toString()
                    )
                    confirmed_textview.setText(
                        "Confirmed:" + DecimalFormat("##,##,###").format(
                            mStates?.get(0)?.confirmed
                        ).toString()
                    )
                    recovered_textview.setText(
                        "Recovered:" + DecimalFormat("##,##,###").format(
                            mStates?.get(0)?.recovered
                        ).toString()
                    )

                    setChartData()


                } else {

                    progress_bar.visibility = View.GONE
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
        confirmedDataSet.setDrawCircles(false)

        deceasedDataSet.color = Color.RED
        deceasedDataSet.setDrawCircles(false)

        recoveredDataSet.color = Color.GREEN
        recoveredDataSet.setDrawCircles(false)

        val mLineData = LineData()

        mLineData.addDataSet(confirmedDataSet)
        mLineData.addDataSet(deceasedDataSet)
        mLineData.addDataSet(recoveredDataSet)

        line_chart.description.text="Number of days"

        line_chart.data= mLineData
        line_chart.invalidate()

    }

    private fun retrofitOperations() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.covid19india.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mCoronaService = retrofit.create(CoronaService::class.java)

    }

}
