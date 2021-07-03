package com.beratyesbek.vhoops.core.utilities.extensions

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.Timestamp

fun TextView.date(timestamp: Timestamp){
  text = timestamp.toDate().toString().substringBefore("GMT+03:00")
}

@BindingAdapter("android:setDate")
fun setDate(textView: TextView,timestamp: Timestamp){
    textView.date(timestamp)
}