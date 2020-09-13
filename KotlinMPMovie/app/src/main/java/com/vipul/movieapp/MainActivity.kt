package com.vipul.movieapp

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Box
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumnItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.state
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.graphics.drawscope.drawCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.picasso.Picasso
import kotlinx.coroutines.ExperimentalCoroutinesApi
import model.Movie

class MainActivity : AppCompatActivity() {

    private val movieViewModel: MovieViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(colors = MaterialTheme.colors) {
                Column {
                    Crossfade(
                        current = movieViewModel.moviesLiveData()
                            .observeAsState(initial = MovieResult.Loading)
                    ) { state ->
                        state.run {
                            when (state.value) {
                                is MovieResult.Loading -> {
                                    LoadingComponent()
                                }
                                is MovieResult.Success -> {
                                    ComponentList(movies = (state.value as MovieResult.Success<List<Movie>>).data)
                                }
                                is MovieResult.Error -> {
                                    Log.i("vipul", "Sample!")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LoadingComponent() {
        Box(modifier = Modifier.fillMaxSize(), gravity = ContentGravity.Center) {
            CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
        }
    }

    @Composable
    private fun ComponentList(movies: List<Movie>) {
        LazyColumnItems(items = movies) { movie ->
            Card(
                shape = RoundedCornerShape(2.dp), backgroundColor = Color.White,
                modifier = Modifier.padding(4.dp)
            ) {
                Row(
                    Modifier.padding(
                        8.dp,
                        8.dp
                    )
                ) {
                    NetworkImageComponentPicasso(url = "http://image.tmdb.org/t/p/w200${movie.poster_path}")
                    Column(
                        modifier = Modifier.padding(
                            0.dp,
                            20.dp,
                            0.dp,
                            0.dp
                        ) + Modifier.weight(1f)
                    ) {
                        Text(
                            text = movie.title,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Text(
                            text = movie.release_date,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        )
                    }
                    Column(
                        modifier = Modifier.width(70.dp) +
                                Modifier.height(200.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.wrapContentWidth(CenterHorizontally),
                            progress = getRating(movie = movie),
                            color = getRatingColor(movie = movie)
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = String.format("%.1f", getRating(movie = movie)),
                            style = TextStyle(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun getRating(movie: Movie) = (movie.vote_average / 10)

    @Composable
    private fun getRatingColor(movie: Movie) = when {
        getRating(movie = movie) < 0.5 -> {
            Color.Red
        }
        else -> {
            Color.Green
        }
    }
}

@Composable
private fun NetworkImageComponentPicasso(url: String) {
    var image by state<ImageAsset?> { null }
    var drawable by state<Drawable?> { null }
    onCommit(url) {
        val picasso = Picasso.get()
        val target = object : com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                drawable = placeHolderDrawable
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                drawable = errorDrawable
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image = bitmap?.asImageAsset()
            }
        }
        picasso
            .load(url)
            .into(target)

        onDispose {
            image = null
            drawable = null
            picasso.cancelRequest(target)
        }
    }

    val theImage = image
    val theDrawable = drawable
    if (theImage != null) {
        Column {
            val imageModifier =
                Modifier.width(150.dp) +
                        Modifier.height(200.dp) +
                        Modifier.clip(RoundedCornerShape(8.dp))
            Image(theImage, modifier = imageModifier)
        }
    } else if (theDrawable != null) {
        Canvas(modifier = Modifier.preferredHeight(100.dp) + Modifier.preferredWidth(150.dp)) {
            drawCanvas { canvas, _ ->
                theDrawable.draw(canvas.nativeCanvas)
            }
        }
    }
}