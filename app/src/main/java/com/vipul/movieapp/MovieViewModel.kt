package com.vipul.movieapp

import MoviesRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.Movie

class MovieViewModel : ViewModel() {

    private val repository = MoviesRepository()

    private val _uiState = MutableStateFlow<MovieResult<List<Movie>>>(MovieResult.Loading)
    val uiState: StateFlow<MovieResult<List<Movie>>> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = MovieResult.Loading
            _uiState.value = try {
                MovieResult.Success(repository.getMovies())
            } catch (e: Exception) {
                MovieResult.Error(e)
            }
        }
    }
}
