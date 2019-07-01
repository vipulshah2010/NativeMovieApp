package com.nativemovieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.Holder>() {

    private val movies: ArrayList<Movie> = arrayListOf()

    var onClick: (Movie) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, null)
            .let { Holder(it as TextView) }

    override fun getItemCount(): Int = movies.count()

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(movies[position])

    fun updateData(list: List<Movie>) {
        movies.clear()
        movies.addAll(list)
        notifyDataSetChanged()
    }

    inner class Holder(private val view: TextView) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClick(movies[adapterPosition])
        }

        fun bind(movie: Movie) {
            view.text = movie.title
        }
    }
}