package com.example.coronatracker.models

import com.google.gson.annotations.SerializedName

class DataPerDay {

    @SerializedName("totaldeceased")
    var totalDeceasedTillNow:Int=0

    @SerializedName("totalconfirmed")
    var totalConfirmedTillNow:Int=0

    @SerializedName("totalrecovered")
    var totalRecoveredTillNow:Int=0

}