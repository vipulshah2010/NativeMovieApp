package com.vipul.movieapp

import MoviesApi
import androidx.compose.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import model.Movie
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MovieViewModel : ViewModel() {

    @ExperimentalCoroutinesApi
    @Composable
    fun moviesLiveData() = liveData { emit(getMovies()) }

    private suspend fun getMovies(): MovieResult<List<Movie>> = suspendCoroutine { continuation ->
        MoviesApi().getMovies(success = {
            continuation.resume(MovieResult.Success(it))
        }, failure = {
            continuation.resume(MovieResult.Error(it ?: Exception("Unknown Error!")))
        })
    }
}