package com.vipul.movieapp

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.vipul.movieapp.ui.theme.MovieAppTheme
import model.Movie
import android.os.Bundle

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieAppTheme {
                MovieApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieApp(viewModel: MovieViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Now Playing") })
        }
    ) { innerPadding ->
        MovieListScreen(
            uiState = uiState,
            onRetry = viewModel::loadMovies,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun MovieListScreen(
    uiState: MovieResult<List<Movie>>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is MovieResult.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is MovieResult.Success -> {
                MovieList(movies = uiState.data)
            }
            is MovieResult.Error -> {
                ErrorState(
                    message = uiState.throwable.message ?: "Unknown error",
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

@Composable
private fun MovieList(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = movies, key = { it.title + it.releaseDate }) { movie ->
            MovieItem(movie = movie)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun MovieItem(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    val posterUrl = "${MoviesRepository.POSTER_BASE_URL}${movie.posterPath}"
    val rating = movie.voteAverage / 10f

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "Poster for ${movie.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, top = 8.dp),
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = movie.releaseDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Column(
                modifier = Modifier
                    .width(56.dp)
                    .height(150.dp)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(
                    progress = { rating },
                    color = if (rating >= 0.5f) Color(0xFF4CAF50) else Color(0xFFF44336),
                    trackColor = (if (rating >= 0.5f) Color(0xFF4CAF50) else Color(0xFFF44336)).copy(alpha = 0.2f),
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(44.dp),
                )
                Text(
                    text = String.format("%.1f", movie.voteAverage),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Failed to load movies",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true, name = "Movie Item")
@Composable
private fun MovieItemPreview() {
    MovieAppTheme {
        MovieItem(
            movie = Movie(
                posterPath = "/test.jpg",
                title = "The Outpost",
                voteAverage = 7.5f,
                releaseDate = "2024-06-24",
                overview = "A small team of US soldiers battles to defend their remote outpost.",
            )
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun LoadingPreview() {
    MovieAppTheme {
        MovieListScreen(
            uiState = MovieResult.Loading,
            onRetry = {},
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun ErrorPreview() {
    MovieAppTheme {
        MovieListScreen(
            uiState = MovieResult.Error(Exception("Network unavailable")),
            onRetry = {},
        )
    }
}
