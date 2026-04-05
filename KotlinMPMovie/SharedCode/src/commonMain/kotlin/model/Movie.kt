package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("poster_path") val posterPath: String,
    val title: String,
    @SerialName("vote_average") val voteAverage: Float,
    @SerialName("release_date") val releaseDate: String,
    val overview: String
)
