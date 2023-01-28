package com.example.roal.routes

import com.example.roal.models.MainUser
import com.example.roal.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsersRoutes {

    @POST("prod/roal_app_login/login")
    fun login(@Body mainUser: MainUser): Call<ResponseHttp>

    @POST("prod/roal_app_login/login")
    fun recoveryPassword(@Body mainUser: MainUser): Call<ResponseHttp>

    @POST("prod/roal_app_login/login")
    fun verificationCode(@Body mainUser: MainUser): Call<ResponseHttp>

    @POST("prod/roal_app_login/login")
    fun resetPassword(@Body mainUser: MainUser): Call<ResponseHttp>

    @GET("prod/get_user/{email}")
    fun getMainData(
        @Path("email") email: String
    ): Call<MainUser>
}