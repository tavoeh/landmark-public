package com.tigerspike.landmark.injection

import com.tigerspike.landmark.data.repository.NoteRepositoryImpl
import com.tigerspike.landmark.data.repository.UserRepositoryImpl
import com.tigerspike.landmark.domain.contract.NoteRepository
import com.tigerspike.landmark.domain.contract.UserRepository
import com.tigerspike.landmark.util.DefaultDispatcherProvider
import com.tigerspike.landmark.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 *  Dependency application module
 **/

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideDispatchers(dispatchers: DefaultDispatcherProvider): DispatcherProvider = dispatchers

    //region Repositories
    @Provides
    @Singleton
    fun provideNoteRepository(repository: NoteRepositoryImpl): NoteRepository = repository

    @Provides
    @Singleton
    fun provideUserRepository(repository: UserRepositoryImpl): UserRepository = repository
    //endregion

}