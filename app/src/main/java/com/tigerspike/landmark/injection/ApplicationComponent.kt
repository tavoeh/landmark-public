package com.tigerspike.landmark.injection

import com.tigerspike.landmark.presentation.MainActivity
import com.tigerspike.landmark.presentation.authentication.SignInFragment
import com.tigerspike.landmark.presentation.authentication.SignUpFragment
import com.tigerspike.landmark.presentation.map.MapFragment
import com.tigerspike.landmark.presentation.notes.SaveNoteFragment
import com.tigerspike.landmark.presentation.search.SearchFragment
import com.tigerspike.landmark.presentation.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 * This class represents the Application dependencies graph
 **/

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(mapFragment: MapFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(saveNoteFragment: SaveNoteFragment)
    fun inject(signInFragment: SignInFragment)
    fun inject(signUpFragment: SignUpFragment)
    fun inject(settingsFragment: SettingsFragment)

}