package com.tigerspike.landmark.presentation

import com.tigerspike.landmark.domain.model.Error

/**
 * Class represents the view state of the object of the system
 */
sealed class ViewState<T> {
    data class Loading<T>(val data: T?) : ViewState<T>()
    data class Data<T>(val data: T) : ViewState<T>()
    data class Failure<T>(val error: Error) : ViewState<T>()
}
