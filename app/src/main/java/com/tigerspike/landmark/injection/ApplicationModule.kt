package com.tigerspike.landmark.injection

import com.tigerspike.landmark.data.repository.NoteRepositoryImpl
import com.tigerspike.landmark.data.repository.UserRepositoryImpl
import com.tigerspike.landmark.domain.contract.NoteRepository
import com.tigerspike.landmark.domain.contract.UserRepository
import com.tigerspike.landmark.util.DefaultDispatcherProvider
import com.tigerspike.landmark.util.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 *  Dependency application module
 **/

@InstallIn(ApplicationComponent::class)
@Module
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindDispatchers(dispatchers: DefaultDispatcherProvider): DispatcherProvider

    //region Repositories
    @Binds
    @Singleton
    abstract fun bindNoteRepository(repository: NoteRepositoryImpl): NoteRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository
    //endregion

}