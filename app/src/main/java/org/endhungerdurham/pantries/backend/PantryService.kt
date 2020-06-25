package org.endhungerdurham.pantries.backend

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET


interface PantryService {
    @GET("pantries")
    fun listPantries(): Call<List<Pantry?>?>?
}