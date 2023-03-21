package com.example.apimovies.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.apimovies.R
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_activity)

        bundle = intent.extras
        val imagen = bundle!!.getString("image")
        val title = bundle!!.getString("title")
        val overview = bundle!!.getString("overview")
        val popularity = bundle!!.getDouble("popularity")
        val votes = bundle!!.getInt("votes")
        val votes_avg = bundle!!.getDouble("votes_avg")

        val imageViewMovie : ImageView = findViewById(R.id.imageDetails)
        Picasso.get().load(imagen).into(imageViewMovie)

        val titleText : TextView= findViewById(R.id.originalTitleDetails)
        titleText.setText(title)

        val overviewText : TextView = findViewById(R.id.overviewDetails)
        overviewText.setText(overview)

        val popularityText : TextView = findViewById(R.id.popularity)
        popularityText.setText(popularity.toString())

        val votesText : TextView = findViewById(R.id.votes)
        votesText.setText(votes.toString())

        val votes_avgText : TextView = findViewById(R.id.votes_avg)
        votes_avgText.setText(votes_avg.toString())

    }
}