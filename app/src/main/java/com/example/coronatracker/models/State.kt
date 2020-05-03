package com.example.coronatracker.models

import com.google.gson.annotations.SerializedName

class State {

    @SerializedName("state")
    lateinit var name: String

    @SerializedName("statecode")
    lateinit var statecode: String

    @SerializedName("deaths")
    var deaths: Int = 0

    @SerializedName("confirmed")
    var confirmed: Int = 0

    @SerializedName("recovered")
    var recovered: Int = 0

}