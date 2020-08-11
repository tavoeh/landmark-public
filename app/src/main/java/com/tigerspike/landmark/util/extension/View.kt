package com.tigerspike.landmark.util.extension

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Created by Gustavo Enriquez on 26/7/20.
 *
 * Extension functions that offer extra functionality to the View class
 **/

val View.inflater: LayoutInflater
    get() = LayoutInflater.from(this.context)

@BindingAdapter("onClick")
fun View.onClick(block: () -> Unit) = setOnClickListener { block() }