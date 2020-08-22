package com.tigerspike.landmark.presentation.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.useCase.GetNotesUseCase
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.DispatcherProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

class MapViewModel @ViewModelInject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _mapViewState = MutableLiveData<ViewState<MapState>>()
    val mapViewState: LiveData<ViewState<MapState>> get() = _mapViewState

    // Initial map state
    private var mapState = MapState(
        notes = listOf(),
        selectedNoteLocation = null,
        mapLastLocation = null,
        isUserLocation = false
    )

    // Keeping the selected market in the viewModel
    // to protect it from configuration changes
    var selectedNoteMarker: Marker? = null

    fun getNotes() {
        viewModelScope.launch(dispatchers.main()) {
            _mapViewState.value = when (val result = getNotesUseCase.execute()) {
                is Result.Success -> {

                    // Notes crested by the user
                    val userNotes = result.data.first.map { note ->
                        MapState.MapNote(
                            id = note.id,
                            text = note.text,
                            location = LatLng(note.latitude, note.longitude),
                            color = HUE_AZURE
                        )
                    }

                    // Notes create by other users
                    val otherNotes = result.data.second.map { note ->
                        MapState.MapNote(
                            id = note.id,
                            text = note.text,
                            location = LatLng(note.latitude, note.longitude),
                            color = HUE_RED
                        )
                    }

                    // Update map state
                    mapState = mapState.copy(notes = userNotes + otherNotes)
                    ViewState.Data(mapState)
                }

                is Result.Failure -> ViewState.Failure(result.error)
            }
        }
    }

    fun setSelectedNoteLocation(location: LatLng?, isUserLocation: Boolean = false) {
        mapState = mapState.copy(
            selectedNoteLocation = location,
            isUserLocation = isUserLocation
        )
        _mapViewState.value = ViewState.Data(mapState)
    }

    fun setMapLastLocation(location: LatLng) {
        mapState = mapState.copy(mapLastLocation = location)
    }

    data class MapState(
        val notes: List<MapNote>,
        val selectedNoteLocation: LatLng?,
        val mapLastLocation: LatLng?,
        val isUserLocation: Boolean
    ) {
        data class MapNote(
            val id: String,
            val text: String,
            val location: LatLng,
            val color: Float
        )
    }
}