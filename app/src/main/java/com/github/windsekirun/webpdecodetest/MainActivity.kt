package com.github.windsekirun.webpdecodetest

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDecoder
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpFrameLoader
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val infinity = findViewById<ImageView>(R.id.imageView1)
        val once = findViewById<ImageView>(R.id.imageView2)

        loadWebpImage(infinity, "https://windsekirun.github.io/Webpdecodetest/webp0.webp")
        loadWebpImage(once, "https://windsekirun.github.io/Webpdecodetest/webp1.webp")
    }

    private fun loadWebpImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(object : CustomTarget<Drawable>() {
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

                    val netscapeLoopCount = getNetscapeLoopCount(resource)

                    Log.d("WebpDecodeTest", "imageUrl $imageUrl loopCount = ${resource.loopCount}")
                    Log.d("WebpDecodeTest", "imageUrl $imageUrl netscapeloopCount = $netscapeLoopCount")

//                    resource.loopCount = resource.loopCount
                    resource.loopCount = netscapeLoopCount
                    resource.start()
                    imageView.setImageDrawable(resource)
                }
            })
    }

    private fun getNetscapeLoopCount(drawable: WebpDrawable): Int {
        val stateField = drawable.javaClass.getDeclaredField("state").apply { isAccessible = true }
        val state = stateField.get(drawable)

        val loaderField = state.javaClass.getDeclaredField("frameLoader").apply { isAccessible = true }
        val loader = loaderField.get(state) as WebpFrameLoader

        val decoderField = loader.javaClass.getDeclaredField("webpDecoder").apply { isAccessible = true }
        val decoder = decoderField.get(loader) as WebpDecoder

        return decoder.netscapeLoopCount
    }
}
