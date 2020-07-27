package com.vipul.movieapp

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onCommit
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.animation.Crossfade
import androidx.ui.core.Alignment.Companion.CenterHorizontally
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.core.setContent
import androidx.ui.foundation.Box
import androidx.ui.foundation.Canvas
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.graphics.drawscope.drawCanvas
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.height
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredWidth
import androidx.ui.layout.width
import androidx.ui.layout.wrapContentWidth
import androidx.ui.livedata.observeAsState
import androidx.ui.material.Card
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.LinearProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import androidx.ui.unit.sp
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
                shape = RoundedCornerShape(2.dp), color = Color.White,
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