package com.github.windsekirun.webpdecodetest

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDecoder
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpFrameLoader
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var infinity: ImageView
    private lateinit var once: ImageView
    private var useNetscape: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        once = findViewById(R.id.imageView2)
        infinity = findViewById(R.id.imageView1)
        val toggleButton = findViewById<Button>(R.id.button)

        toggleButton.setOnClickListener {
            useNetscape = !useNetscape
            toggleButton.text = "Toggle Mode: ${if (useNetscape) "NetscapeLoopCount" else "LOOP_INTRINSIC"}"

            loadWebpImage(infinity, "https://windsekirun.github.io/Webpdecodetest/webp0.webp")
            loadWebpImage(once, "https://windsekirun.github.io/Webpdecodetest/webp1.webp")
        }

        loadWebpImage(infinity, "https://windsekirun.github.io/Webpdecodetest/webp0.webp")
        loadWebpImage(once, "https://windsekirun.github.io/Webpdecodetest/webp1.webp")
    }

    private fun loadWebpImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    if (resource !is WebpDrawable) {
                        imageView.setImageDrawable(resource)
                        return
                    }

                    val netscapeLoopCount = getNetscapeLoopCount(resource)

                    /*
                     * it will use WebpDecoder.getTotalIterationCount
                     * But, it use `mWebPImage.getFrameCount() + 1;` in internally
                     * when WebpImage.loopCount isn't 0
                     */
                    resource.loopCount = WebpDrawable.LOOP_INTRINSIC
                    Timber.d("imageUrl $imageUrl INTRINSIC = ${resource.loopCount}")
                    Timber.d("imageUrl $imageUrl NetscapeLoopCount = $netscapeLoopCount")

                    if (useNetscape) {
                        resource.loopCount = netscapeLoopCount
                    } else {
                        resource.loopCount = WebpDrawable.LOOP_INTRINSIC
                    }

                    resource.start()
                    imageView.setImageDrawable(resource)
                }
            })
    }

    private fun getNetscapeLoopCount(drawable: WebpDrawable): Int {
        val stateField = drawable.javaClass.getDeclaredField("state").apply { isAccessible = true }
        val state = stateField.get(drawable)

        val loaderField =
            state.javaClass.getDeclaredField("frameLoader").apply { isAccessible = true }
        val loader = loaderField.get(state) as WebpFrameLoader

        val decoderField =
            loader.javaClass.getDeclaredField("webpDecoder").apply { isAccessible = true }
        val decoder = decoderField.get(loader) as WebpDecoder

        return decoder.netscapeLoopCount
    }
}
