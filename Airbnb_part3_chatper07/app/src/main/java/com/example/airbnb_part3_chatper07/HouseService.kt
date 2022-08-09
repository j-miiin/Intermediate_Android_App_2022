package com.example.airbnb_part3_chatper07

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {

    @GET("")
    fun getHouseList(): Call<HouseDto>
}