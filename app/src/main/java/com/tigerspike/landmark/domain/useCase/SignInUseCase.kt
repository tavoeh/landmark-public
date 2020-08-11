package com.tigerspike.landmark.domain.useCase

import com.tigerspike.landmark.domain.contract.UserRepository
import com.tigerspike.landmark.domain.model.Result
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(email: String, password: String): Result<Unit> {
        return userRepository.signIn(email, password)
    }
}