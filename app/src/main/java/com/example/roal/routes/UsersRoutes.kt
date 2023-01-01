package com.example.roal.routes

import com.example.roal.models.MainUser
import com.example.roal.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UsersRoutes {

    @POST("prod/roal_app_login/login")
    fun login(@Body mainUser: MainUser): Call<ResponseHttp>

    @POST("prod/roal_app_login/login")
    fun recoveryPassword(@Body mainUser: MainUser): Call<ResponseHttp>

    @POST("prod/roal_app_login/login")
    fun verificationCode(@Body mainUser: MainUser): Call<ResponseHttp>

    @POST("prod/roal_app_login/login")
    fun resetPassword(@Body mainUser: MainUser): Call<ResponseHttp>

}