package com.example.apimovies.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apimovies.ApiService
import com.example.apimovies.Model.Movie
import com.example.apimovies.MoviesAdapter
import com.example.apimovies.Util
import com.example.apimovies.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MoviesAdapter
    private val moviesImagen = mutableListOf<Movie>()
    private var isLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val content : View = findViewById(androidx.appcompat.R.id.content)
//        content.viewTreeObserver.addOnPreDrawListener(
//            object : ViewTreeObserver.OnPreDrawListener {
//                override fun onPreDraw(): Boolean {
//                    return if(viewModel.isReady) {
//                       content.viewTreeObserver.removeOnPreDrawListener(this)
//                       true
//                    }else{
//                       false
//                    }
//                }
//            }
//        )


        screenSplash.setKeepOnScreenCondition { isLoading }

        initRecycleView()
        getMoviesAPI()

    }

    private fun initRecycleView() {
        binding.recycleView.layoutManager = GridLayoutManager(this, 2)
        adapter = MoviesAdapter(moviesImagen)
        binding.recycleView.adapter = adapter

        adapter.onClick = {
            val i = Intent(this, DetailsActivity::class.java)
            val imagen = moviesImagen.get(binding.recycleView.getChildAdapterPosition(it)).image
            val title = moviesImagen.get(binding.recycleView.getChildAdapterPosition(it)).title
            val overview =
                moviesImagen.get(binding.recycleView.getChildAdapterPosition(it)).overview
            val popularity =
                moviesImagen.get(binding.recycleView.getChildAdapterPosition(it)).popularity
            val votes = moviesImagen.get(binding.recycleView.getChildAdapterPosition(it)).vote_count
            val votes_avg =
                moviesImagen.get(binding.recycleView.getChildAdapterPosition(it)).vote_average


            // pass the transition name and other data to the intent
            i.putExtra("image", imagen)
            i.putExtra("title", title)
            i.putExtra("overview", overview)
            i.putExtra("popularity", popularity)
            i.putExtra("votes", votes)
            i.putExtra("votes_avg", votes_avg)

            startActivity(i)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun getMoviesAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            val call = getRetrofit().create(ApiService::class.java)
                .getMovies(key = "9fbea8d0a3e8c5d1ab5573cb33b9282c", leng = "en-US")
            runOnUiThread {
                if (call.isSuccessful) {
                    // showRecycleView
                    val movies = call.body()
                    val results = movies?.results ?: emptyList()
                    moviesImagen.clear()
                    val listMovies: List<Movie> = results.map {
                        Movie(
                            Util.baseUrlImage + it.poster_path,
                            it.title,
                            it.overview,
                            it.popularity,
                            it.vote_count.toDouble(),
                            it.vote_average.toInt()
                        )
                    }
                    moviesImagen.addAll(listMovies)
                    adapter.notifyDataSetChanged()
                    isLoading = false
                } else {
                    // show error
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}