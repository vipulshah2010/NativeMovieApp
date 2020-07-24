package model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val title: String,
    val release_date: String,
    val poster_path: String
)