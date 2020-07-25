package model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val poster_path: String,
    val title: String,
    val vote_average: Float,
    val release_date: String,
    val overview: String
)