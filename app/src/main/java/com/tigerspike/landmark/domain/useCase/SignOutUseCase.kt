package com.tigerspike.landmark.domain.useCase

import com.tigerspike.landmark.domain.contract.UserRepository
import com.tigerspike.landmark.domain.model.Result
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(): Result<Unit> {
        return userRepository.signOut()
    }
}