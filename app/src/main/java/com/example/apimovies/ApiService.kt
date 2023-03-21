package com.example.apimovies

import com.example.apimovies.Model.ResultsMovies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("3/movie/popular")
    suspend fun getMovies(@Query("api_key") key:String, @Query("language") leng:String):Response<ResultsMovies>
}