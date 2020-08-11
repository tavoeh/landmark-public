package com.tigerspike.landmark.domain.model

import java.lang.Exception

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 *  Class for handling errors/failures/exceptions.
 */

sealed class Error {
    data class DomainError(val exception: Exception) : Error()
}