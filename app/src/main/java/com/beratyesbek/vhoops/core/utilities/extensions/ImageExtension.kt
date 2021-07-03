package com.beratyesbek.vhoops.core.utilities.extensions

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.beratyesbek.vhoops.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.downloadFromUrl(url: String?, progressBar: CircularProgressDrawable) {
        val options = RequestOptions()
            .placeholder(progressBar)
            .error(R.drawable.ic_profile_blue)

        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(this)

}

fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("android:downloadUrl")
fun downloadImage(imageView: ImageView, url: String) {
    imageView.downloadFromUrl(url, placeHolderProgressBar(imageView.context))
}