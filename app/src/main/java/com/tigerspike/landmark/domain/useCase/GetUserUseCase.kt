package com.tigerspike.landmark.domain.useCase

import com.tigerspike.landmark.domain.contract.UserRepository
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.model.User
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(): Result<User> {
        return userRepository.getUser()
    }
}