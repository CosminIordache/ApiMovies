package com.example.apimovies.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apimovies.R
import com.example.apimovies.data.ApiService
import com.example.apimovies.model.Movie
import com.example.apimovies.data.Util
import com.example.apimovies.databinding.ActivityMainBinding
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.*
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
        lifecycleScope.launch {
            initRecycleView()
            getMoviesAPI()
        }


    }

    private fun initRecycleView() {
        binding.recycleView.layoutManager = GridLayoutManager(this, 2)
        adapter = MoviesAdapter(moviesImagen)
        binding.recycleView.adapter = adapter

        // onClick in a itemView gives all info and go to another activity

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


            // gives all the attributes that have a movie
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

    private suspend fun getMoviesAPI() {
        //CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java)
                .getMovies(key = "9fbea8d0a3e8c5d1ab5573cb33b9282c", leng = "en-US")

            withContext(Dispatchers.Main) {
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
        //}
    }

    override fun onCreateOptionsMenu(
        menu: Menu?
    ): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val switchItem = menu!!.findItem(R.id.menu_switch)
        val switchView : SwitchMaterial= switchItem?.actionView!!.findViewById(R.id.switch_item)

        switchView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableDarkMode()
                switchView.isChecked = true
            } else {
                disableDarkMode()
                switchView.isChecked = false
            }
        }

        return true
    }

    private fun enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }

    private fun disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }

}