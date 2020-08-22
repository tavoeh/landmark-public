package com.tigerspike.landmark.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.model.User
import com.tigerspike.landmark.domain.useCase.GetUserUseCase
import com.tigerspike.landmark.util.Event
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

class MainViewModel @ViewModelInject constructor(
    getUserUseCase: GetUserUseCase
) : ViewModel() {

    /**
     * Mutable live data that triggers a user state update
     */
    private val _updateUserState = MutableLiveData<Unit>()

    /**
     * User Global state LiveData object
     */
    val userState = _updateUserState.switchMap {
        liveData {
            val userState = getUserUseCase.execute().toUserState()
            emit(userState)
        }
    }

    /**
     * Even triggered when a note is saved
     */
    private val _onSaveNoteEvent = MutableLiveData<Event<Unit>>()
    val onSaveNoteEvent: LiveData<Event<Unit>> get() = _onSaveNoteEvent

    init {
        updateUserState()
    }

    fun updateUserState() {
        _updateUserState.value = Unit
    }

    fun onSaveNote() {
        _onSaveNoteEvent.value = Event()
    }

    private fun Result<User>.toUserState() = when (this) {
        is Result.Success -> data
        is Result.Failure -> User.Guest
    }

}