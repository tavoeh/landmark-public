package com.tigerspike.landmark

import android.app.Application
import com.tigerspike.landmark.injection.ApplicationComponent
import com.tigerspike.landmark.injection.DaggerApplicationComponent

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

class LandMarkApp : Application() {

    // Dependency Injection graph component
    val appComponent: ApplicationComponent by lazy { DaggerApplicationComponent.create() }
}