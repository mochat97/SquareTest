package com.mshaw.squaretest.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    if (url == null || url.isEmpty()) return
    view.load(url)
}