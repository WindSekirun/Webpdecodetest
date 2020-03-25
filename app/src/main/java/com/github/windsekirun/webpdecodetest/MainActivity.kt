package com.github.windsekirun.webpdecodetest

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

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
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(object: CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    if (resource !is WebpDrawable) {
                        imageView.setImageDrawable(resource)
                        return
                    }

                    Log.d("WebpDecodeTest", "imageUrl $imageUrl loopCount = ${resource.loopCount}")

                    resource.start()
                    imageView.setImageDrawable(resource)
                }
            })
    }
}
