package com.example.guru2022

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var AddMovie: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AddMovie = findViewById(R.id.AddMovie)

        AddMovie.setOnClickListener {
            val intent = Intent(this, newFeelim::class.java)
            startActivity(intent)
        }
    }
}

