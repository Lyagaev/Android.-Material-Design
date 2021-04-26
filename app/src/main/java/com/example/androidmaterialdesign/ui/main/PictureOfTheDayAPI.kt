package com.example.androidmaterialdesign.ui.main

import com.example.androidmaterialdesign.model.PODServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(
            @Query("date") date: String,
            @Query("api_key") apiKey: String): Call<PODServerResponseData>
}