package com.example.roal.api

import com.example.roal.routes.UsersRoutes
import com.example.roal.routes.WorkersRoutes

class ApiRoutes {

    private val API_URL = "https://nbyz455qc9.execute-api.us-east-1.amazonaws.com/"

    private val retrofit = RetrofitClient()

    fun getMainUserRoutes(): UsersRoutes {
        return retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }

    fun getWorkersRoutes(): WorkersRoutes {
        return retrofit.getClient(API_URL).create(WorkersRoutes::class.java)
    }
}