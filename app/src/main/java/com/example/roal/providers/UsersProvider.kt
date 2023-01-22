package com.example.roal.providers

import com.example.roal.api.ApiRoutes
import com.example.roal.models.MainUser
import com.example.roal.models.ResponseHttp
import com.example.roal.routes.UsersRoutes
import com.google.gson.Gson
import retrofit2.Call

class UsersProvider {

    private var usersRoutes: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        usersRoutes = api.getMainUserRoutes()
    }

    fun login(mainUser: MainUser): Call<MainUser>? {
        return usersRoutes?.login(mainUser)
    }

    fun recoveryPassword(mainUser: MainUser): Call<ResponseHttp>? {
        return usersRoutes?.recoveryPassword(mainUser)
    }

    fun verificationCode(mainUser: MainUser): Call<ResponseHttp>? {
        return usersRoutes?.verificationCode(mainUser)
    }

    fun resetPassword(mainUser: MainUser): Call<ResponseHttp>? {
        return usersRoutes?.resetPassword(mainUser)
    }
}