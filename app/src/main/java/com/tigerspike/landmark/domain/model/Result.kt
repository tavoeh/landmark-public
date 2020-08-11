package com.tigerspike.landmark.domain.model

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 * Result class that holds successful success or failure
 */

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val error: Error) : Result<T>()
}