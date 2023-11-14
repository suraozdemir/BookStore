package com.example.bookstore.common

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.bookstore.data.util.CreditCardExpiryInputFilter
import com.example.bookstore.data.util.CreditCardFormatTextWatcher
import com.google.android.material.textfield.TextInputEditText


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).into(this)
}

fun TextView.setStrikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextInputEditText.setCreditCardTextWatcher() {
    val tv = CreditCardFormatTextWatcher(this)
    this.addTextChangedListener(tv)
}

fun TextInputEditText.setExpiryDateFilter() {
    this.filters = arrayOf(CreditCardExpiryInputFilter())
}