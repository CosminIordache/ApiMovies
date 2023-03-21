package com.example.apimovies.Model

import com.example.apimovies.Model.Result

data class ResultsMovies(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)