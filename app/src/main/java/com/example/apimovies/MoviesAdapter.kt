package com.example.apimovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apimovies.Model.Movie
import com.example.apimovies.databinding.CardMovieBinding
import com.squareup.picasso.Picasso


class MoviesAdapter(var images: List<Movie>  ) :
RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    lateinit var onClick: (View) -> Unit

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardMovieBinding.bind(itemView)
        val tilteMovie : TextView = itemView.findViewById(R.id.titleMovie)
        fun bind(image:String, title: String, onClick: (View) -> Unit) = with(itemView) {
            Picasso.get().load(image).into(binding.imageMovie)
            tilteMovie.setText(title)

            setOnClickListener { onClick(itemView) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val imagenView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_movie, viewGroup, false)
        return ViewHolder(imagenView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val items = images[i]
        viewHolder.itemView.transitionName = "$i"
        viewHolder.bind(items.image, items.title, onClick)
    }



    override fun getItemCount(): Int {
        return images.size
    }


}