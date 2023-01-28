package com.example.roal.routes

import com.example.roal.models.ResponseHttp
import com.example.roal.models.Workers
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkersRoutes {

    @GET("prod/get_worker/{dni}")
    fun getWorkers(
        @Query("dni") dni: String
    ):  Call<Workers>

    @POST("prod/register_worker")
    fun creatingWorkers(
        @Body workerUser: Workers
    ):  Call<ResponseHttp>

    @DELETE("prod/delete_worker/{dni}")
    fun deleteWorker(
        @Path("dni") dni: String
    ):  Call<Workers>

    @POST("prod/consult_photo")
    fun consultByPhoto(
        @Body workerUser: Workers
    ):  Call<Workers>
}