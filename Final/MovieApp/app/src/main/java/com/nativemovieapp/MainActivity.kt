package com.nativemovieapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var api: MoviesApi

    override val coroutineContext: CoroutineContext
        get() = job + Main

    private val adapter = MoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job = Job()

        api = MoviesApi()

        setupRecyclerView()

        loadList()
    }

    private fun loadList() {
        api.getMovies(success = {
            launch(Main) { }
        }, failure = {
            handleError(it)
        })
        api.getMovies(
            success = { launch(Main) { adapter.updateData(it) } },
            failure = ::handleError
        )
    }

    private fun setupRecyclerView() {
        adapter.onClick = {
            loadMoviePoster(it.poster_path)
        }

        moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        moviesRecyclerView.adapter = adapter
        moviesRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    private fun loadMoviePoster(posterPath: String) {
        api.getMoviePoster(
            posterPath,
            success = { launch(Main) { moviePosterImageView.setImageBitmap(it) } },
            failure = ::handleError
        )
    }

    private fun handleError(ex: Throwable?) {
        ex?.printStackTrace()
        launch(Main) {
            val msg = ex?.message ?: "Unknown error"
            Snackbar.make(root_view, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry") { loadList() }
                .show()
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
