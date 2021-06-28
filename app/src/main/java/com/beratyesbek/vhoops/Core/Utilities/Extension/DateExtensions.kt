package com.beratyesbek.vhoops.Core.Utilities.Extension

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

fun TextView.date(timestamp: Timestamp){
  text = timestamp.toDate().toString().substringBefore("GMT+03:00")
}

@BindingAdapter("android:setDate")
fun setDate(textView: TextView,timestamp: Timestamp){
    textView.date(timestamp)
}