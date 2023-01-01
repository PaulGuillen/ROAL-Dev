package com.example.roal.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class ResponseHttp(
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: JsonObject,
    @SerializedName("error") val error: String,
) {

    override fun toString(): String {
        return "ResponseHttp(message='$message', code=$code, error='$error')"
    }
}