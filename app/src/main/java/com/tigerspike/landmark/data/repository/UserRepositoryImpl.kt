package com.tigerspike.landmark.data.repository

import com.tigerspike.landmark.data.datasource.UserRemoteDataSource
import com.tigerspike.landmark.data.mapper.toUser
import com.tigerspike.landmark.domain.contract.UserRepository
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.model.User
import com.tigerspike.landmark.util.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 * UserRepository implementation according to the Domain contract
 **/

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val dispatchers: DispatcherProvider
) : UserRepository {

    override suspend fun getUser(): Result<User> =
        withContext(dispatchers.io()) {
            val user = remoteDataSource.getUser()
            Result.Success(
                when {
                    user != null -> user.toUser()
                    else -> User.Guest
                }
            )
        }

    override suspend fun signIn(email: String, password: String): Result<Unit> =
        withContext(dispatchers.io()) {
            remoteDataSource.signIn(email, password)
        }


    override suspend fun signUp(name: String, email: String, password: String): Result<Unit> =
        withContext(dispatchers.io()) {
            remoteDataSource.signUp(name, email, password)
        }

    override suspend fun signOut(): Result<Unit> =
        withContext(dispatchers.io()) {
            remoteDataSource.signOut()
        }

}