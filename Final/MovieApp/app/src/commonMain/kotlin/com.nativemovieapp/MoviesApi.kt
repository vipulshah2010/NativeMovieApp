package com.nativemovieapp

import com.nativemovieapp.shared.ApplicationDispatcher
import com.nativemovieapp.shared.Image
import com.nativemovieapp.shared.toNativeImage
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MoviesApi {

    private val httpClient = HttpClient()

    fun getMovies(success: (List<Movie>) -> Unit, failure: (Throwable?) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
            try {
                val url = "https://api.themoviedb.org/3/movie/now_playing?api_key=55957fcf3ba81b137f8fc01ac5a31fb5"
                val json = httpClient.get<String>(url)
                Json.nonstrict.parse(Movies.serializer(), json)
                    .results
                    .also(success)
            } catch (ex: Exception) {
                failure(ex)
            }
        }
    }

    fun getMoviePoster(posterPath: String, success: (Image?) -> Unit, failure: (Throwable?) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
            try {
                val url = "https://image.tmdb.org/t/p/w780$posterPath"
                httpClient.get<ByteArray>(url)
                    .toNativeImage()
                    .also(success)
            } catch (ex: Exception) {
                failure(ex)
            }
        }
    }
}