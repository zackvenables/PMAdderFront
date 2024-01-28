package com.example.pmadderfront

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pmadderfront.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        var urls = JSONArray()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val editText: EditText = findViewById(R.id.searchInput)
        val searchButton: ImageButton  = findViewById(R.id.searchButton)


        val utils = Utilities()
        searchButton.setOnClickListener {
            val enteredData: String = editText.text.toString()
            urls = JSONArray()
            GlobalScope.launch {
                val result = utils.searchAsync(enteredData).await()
                val jsonObject = JSONObject(result)
                val data = jsonObject.getJSONObject("data")

                val count = data.get("movie_count")

                if(count is Int && count > 0){
                    val movies = data.getJSONArray("movies")
                    val length = if (movies.length() > 4) 2 else movies.length()-1

                    var text = ""
                    for(i in 0..length ) {
                        val movie = movies.getJSONObject(i).get("title_long")
                        text += "$movie"
                        createButtonDynamically(text, i)
                        urls.put(i, movies.getJSONObject(i).getJSONArray("torrents").getJSONObject(0).get("url"))
                    }
                } else {
                    Log.v("error", "nothing found")
                }
            }
        }

        val buttonZero: Button = findViewById(R.id.button0)
        buttonZero.setOnClickListener {
            if (urls.length() > 0) {
                val url = urls.get(0)
                utils.addAsync("$url")
            }
        }

        val buttonOne: Button = findViewById(R.id.button1)
        buttonOne.setOnClickListener {
            if(urls.length() > 1) {
                val url = urls.get(1)
                utils.addAsync("$url")
            }
        }

        val buttonTwo: Button = findViewById(R.id.button2)
        buttonTwo.setOnClickListener {
            if(urls.length() > 2) {
                val url = urls.get(2)
                utils.addAsync("$url")
            }
        }

    }

    private fun createButtonDynamically( text: String, i: Int) {
        var details: TextView
        if (i == 0) {
            details = findViewById(R.id.textView0)
        }else if (i == 1) {
            details = findViewById(R.id.textView1)
        } else {
            details = findViewById(R.id.textView2)
        }
        details.text = text
    }
}
