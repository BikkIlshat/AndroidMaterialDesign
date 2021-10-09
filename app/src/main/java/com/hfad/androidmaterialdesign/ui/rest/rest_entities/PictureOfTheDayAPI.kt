package com.hfad.androidmaterialdesign.ui.rest.rest_entities



import com.hfad.androidmaterialdesign.ui.rest.rest_entities.PODServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODServerResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("date") date: String,
        @Query("api_key") apiKey: String): Call<PODServerResponseData>
}