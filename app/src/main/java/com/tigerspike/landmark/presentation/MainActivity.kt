package com.tigerspike.landmark.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tigerspike.landmark.R
import com.tigerspike.landmark.util.extension.app

/**
 * App's entry point
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        app.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}