package com.tigerspike.landmark.presentation.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tigerspike.landmark.domain.model.Error
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.useCase.SignInUseCase
import com.tigerspike.landmark.domain.useCase.SignOutUseCase
import com.tigerspike.landmark.domain.useCase.SignUpUseCase
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.DispatcherProvider
import com.tigerspike.landmark.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

class SettingsViewModel @ViewModelInject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState<Unit>>()
    val viewState: LiveData<ViewState<Unit>> get() = _viewState

    fun signOut() {
        viewModelScope.launch(dispatchers.main()) {
            _viewState.value = when (val result = signOutUseCase.execute()) {
                is Result.Success -> ViewState.Data(result.data)
                is Result.Failure -> ViewState.Failure(result.error)
            }

        }
    }

}