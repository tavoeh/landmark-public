package com.tigerspike.landmark.data.mapper

import com.tigerspike.landmark.domain.model.Error
import com.tigerspike.landmark.domain.model.Result

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

fun mapError(e: Exception): Error = when (e) {

    //todo: Map other type of exceptions to DomainErrors

    else -> Error.DomainError(e)
}

inline fun <R> resultCatching(block: () -> R): Result<R> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Failure(mapError(e))
    }
}