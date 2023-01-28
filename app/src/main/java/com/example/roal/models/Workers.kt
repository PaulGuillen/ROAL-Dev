package com.example.roal.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Workers(
    @SerializedName("dni") val dni: String? = null,
    @SerializedName("name") val name: String ? = null,
    @SerializedName("lastname") val lastname: String ? = null,
    @SerializedName("date_birth") val date_birth: String ? = null,
    @SerializedName("date_join") val date_join: String ? = null,
    @SerializedName("area") val area: String ? = null,
    @SerializedName("blood_type") val blood_type: String ? = null,
    @SerializedName("diseases") val diseases: String ? = null,
    @SerializedName("allergies") val allergies: String ? = null,
    @SerializedName("phone") val phone: String ? = null,
    @SerializedName("phone_emergency") val phone_emergency: String ? = null,
    @SerializedName("photo") val photo: String ? = null,
    @SerializedName("photo_format") val photoFormat: String ? = null
){

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Workers(dni=$dni, name=$name, lastname=$lastname, date_birth=$date_birth, date_join=$date_join, area=$area, blood_type=$blood_type, diseases=$diseases, allergies=$allergies, phone=$phone, phone_emergency=$phone_emergency, photo=$photo)"
    }
}
