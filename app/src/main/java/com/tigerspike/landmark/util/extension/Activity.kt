package com.tigerspike.landmark.util.extension

import android.app.Activity
import com.tigerspike.landmark.LandMarkApp

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 * Extension functions that offer extra functionality to the Activity class
 **/

val Activity.app: LandMarkApp
    get() = application as LandMarkApp