package com.tigerspike.landmark.presentation.authentication

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tigerspike.landmark.domain.model.Error
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.useCase.SignInUseCase
import com.tigerspike.landmark.domain.useCase.SignUpUseCase
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.DispatcherProvider
import kotlinx.coroutines.launch

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

class AuthenticationViewModel @ViewModelInject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val dispatchers: DispatcherProvider

) : ViewModel() {

    private val _authViewState = MutableLiveData<ViewState<Unit>>()
    val authViewState: LiveData<ViewState<Unit>> get() = _authViewState

    /**
     * LiveData objects to hold the user information
     */
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun signIn() {
        viewModelScope.launch(dispatchers.main()) {

            // Validating email and password values before signing in
            if (email.value.isNullOrBlank() || password.value.isNullOrBlank()) {
                _authViewState.value = ViewState.Failure(Error.DomainError(Exception()))
                return@launch
            }

            _authViewState.value = ViewState.Loading(Unit)
            _authViewState.value = when (val result = signInUseCase.execute(email.value!!, password.value!!)) {
                is Result.Success -> ViewState.Data(result.data)
                is Result.Failure -> ViewState.Failure(result.error)
            }
        }
    }

    fun signUp() {
        viewModelScope.launch(dispatchers.main()) {
            // Validating name, email and password values before signing un
            if (name.value.isNullOrBlank() || email.value.isNullOrBlank() || password.value.isNullOrBlank()) {
                _authViewState.value = ViewState.Failure(Error.DomainError(Exception()))
                return@launch
            }

            _authViewState.value = ViewState.Loading(Unit)
            _authViewState.value = when (val result = signUpUseCase.execute(name.value!!, email.value!!, password.value!!)) {
                is Result.Success -> ViewState.Data(result.data)
                is Result.Failure -> ViewState.Failure(result.error)
            }
        }
    }
}