package com.tigerspike.landmark.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tigerspike.landmark.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * App's entry point
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}