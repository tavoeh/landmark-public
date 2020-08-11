package com.tigerspike.landmark.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tigerspike.landmark.injection.viewModel.ViewModelFactory
import com.tigerspike.landmark.injection.viewModel.ViewModelKey
import com.tigerspike.landmark.presentation.MainViewModel
import com.tigerspike.landmark.presentation.authentication.AuthenticationViewModel
import com.tigerspike.landmark.presentation.map.MapViewModel
import com.tigerspike.landmark.presentation.notes.SaveNoteViewModel
import com.tigerspike.landmark.presentation.search.SearchViewModel
import com.tigerspike.landmark.presentation.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 * Dependency injection viewModel module
 **/

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(viewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SaveNoteViewModel::class)
    abstract fun bindSaveNoteViewModel(viewModel: SaveNoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    abstract fun bindAuthenticationViewModel(viewModel: AuthenticationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}