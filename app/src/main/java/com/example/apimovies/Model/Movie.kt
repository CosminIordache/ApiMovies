package com.example.apimovies.Model

data class Movie(
    var image : String,
    var title : String,
    val overview: String,
    val popularity: Double,
    val vote_average: Double,
    val vote_count: Int,
)
