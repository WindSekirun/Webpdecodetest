package com.github.windsekirun.webpdecodetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val infinity = findViewById<ImageView>(R.id.imageView1)
        val once = findViewById<ImageView>(R.id.imageView2)

        loadWebpImage(infinity, "")
        loadWebpImage(once, "")
    }

    private fun loadWebpImage(imageView: ImageView, imageUrl: String) {

    }
}
