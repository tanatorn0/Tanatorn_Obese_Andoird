package com.example.obese

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ObeseApiService {
    @FormUrlEncoded
    @POST("/api/Obese")
    fun predictObesity(
        @Field("age") age: String,
        @Field("Gender") gender: String,
        @Field("Height") height: String,
        @Field("Weight") weight: String
    ): Call<ObesityResponse>
}
