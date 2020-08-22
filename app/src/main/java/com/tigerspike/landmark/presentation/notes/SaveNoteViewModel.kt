package com.tigerspike.landmark.presentation.notes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.useCase.SaveNoteUseCase
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.DispatcherProvider
import com.tigerspike.landmark.util.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

class SaveNoteViewModel @ViewModelInject constructor(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _onSaveNoteEvent = MutableLiveData<Event<ViewState<Unit>>>()
    val onSaveNoteEvent: LiveData<Event<ViewState<Unit>>> get() = _onSaveNoteEvent

    fun saveNote(text: String, location: LatLng) {
        viewModelScope.launch(dispatchers.main()) {
            _onSaveNoteEvent.value = Event(ViewState.Loading(Unit))
            _onSaveNoteEvent.value = when (val result = saveNoteUseCase.execute(text, location.latitude, location.longitude)) {
                is Result.Success -> Event(ViewState.Data(result.data))
                is Result.Failure -> Event(ViewState.Failure(result.error))
            }
        }
    }

}