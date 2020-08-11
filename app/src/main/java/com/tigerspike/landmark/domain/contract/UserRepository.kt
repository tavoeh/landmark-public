package com.tigerspike.landmark.domain.contract

import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.model.User

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 * UserRepository domain contract
 **/

interface UserRepository {
    suspend fun getUser(): Result<User>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(name: String, email: String, password: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
}