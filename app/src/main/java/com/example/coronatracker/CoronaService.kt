package com.example.coronatracker

import com.example.coronatracker.models.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface CoronaService {

    @GET("data.json")
    fun getResults(): Call<ApiResponse>

}