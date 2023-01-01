package com.example.roal.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class MainUser(
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("action") val action: String? = null,
    @SerializedName("code") val code: Int ? = null,
    @SerializedName("new_password") val new_password: String? = null
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "MainUser(email=$email, password=$password, action=$action, code=$code, new_password=$new_password)"
    }

}