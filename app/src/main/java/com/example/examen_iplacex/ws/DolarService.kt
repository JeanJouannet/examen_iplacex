package com.example.examen_iplacex.ws

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Calendar

interface DolarService {
    //https://mindicador.cl/api/dolar/fecha
    @GET("dolar")
    suspend fun getDolar(): Dolar



}

