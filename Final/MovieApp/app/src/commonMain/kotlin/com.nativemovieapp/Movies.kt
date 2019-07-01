package com.nativemovieapp

import kotlinx.serialization.Serializable

@Serializable
data class Movies(
    val results: List<Movie>
)