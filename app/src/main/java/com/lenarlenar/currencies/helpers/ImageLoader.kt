package com.lenarlenar.currencies.helpers

import android.widget.ImageView
import com.squareup.picasso.Picasso
import javax.inject.Inject

interface ImageLoader {
    fun load(imageView: ImageView, imageUrl: String)
}

class ImageLoaderImp @Inject constructor() : ImageLoader {
    override fun load(imageView: ImageView, imageUrl: String) {
        Picasso.get().load(imageUrl).into(imageView)
    }
}