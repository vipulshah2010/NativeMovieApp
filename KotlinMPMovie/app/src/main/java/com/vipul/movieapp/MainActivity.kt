package com.vipul.movieapp

import MoviesApi
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MoviesApi().getMovies(success = {
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Success!", Toast.LENGTH_SHORT).show()
            }
        }, failure = {
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Failure!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}