package com.example.roal.providers

import com.example.roal.api.ApiRoutes
import com.example.roal.models.ResponseHttp
import com.example.roal.models.Workers
import com.example.roal.routes.WorkersRoutes
import retrofit2.Call

class WorkersProvider {

    private var workersRoutes: WorkersRoutes? = null

    init {
        val api = ApiRoutes()
        workersRoutes = api.getWorkersRoutes()
    }

    fun getWorkers(dni: String): Call<Workers>? {
        return workersRoutes?.getWorkers(dni)
    }
    fun postWorkers(workerUser : Workers): Call<ResponseHttp>? {
        return workersRoutes?.creatingWorkers(workerUser)
    }
    fun deleteWorker(dni: String): Call<Workers>? {
        return workersRoutes?.deleteWorker(dni)
    }
    fun consultByPhoto(workerUser : Workers): Call<Workers>? {
        return workersRoutes?.consultByPhoto(workerUser)
    }
}