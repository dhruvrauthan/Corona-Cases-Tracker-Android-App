package com.example.coronatracker.models

import com.google.gson.annotations.SerializedName

class ApiResponse {

    @SerializedName("cases_time_series")
    lateinit var dataPerDays: List<DataPerDay>

    @SerializedName("statewise")
    lateinit var states: List<State>

}