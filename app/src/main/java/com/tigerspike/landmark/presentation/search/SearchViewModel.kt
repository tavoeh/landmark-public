package com.tigerspike.landmark.presentation.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.tigerspike.landmark.domain.model.Note
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.useCase.SearchNotesByTextOrUserUseCase
import com.tigerspike.landmark.presentation.ViewState
import com.tigerspike.landmark.util.DispatcherProvider

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

class SearchViewModel @ViewModelInject constructor(
    private val searchNotesByTextOrUserUseCase: SearchNotesByTextOrUserUseCase,
    dispatchers: DispatcherProvider
) : ViewModel() {

    /**
     * Search query received from the view that triggers a new search
     */
    val query = MutableLiveData<String>()

    /**
     * LiveData object that returns the results in the form of a view state
     */
    val notesViewState: LiveData<ViewState<List<Item>>> = query.switchMap { query ->
        liveData(dispatchers.main()) {
            val state: ViewState<List<Item>> = when (query.isNotBlank()) {
                true -> searchNotesByTextOrUserUseCase.execute(query).toMapViewState()
                else -> ViewState.Data(listOf())
            }
            emit(state)
        }
    }

    private fun Result<List<Note>>.toMapViewState(): ViewState<List<Item>> =
        when (this) {
            is Result.Success -> {
                val items = data.map { note ->
                    Item(
                        id = note.id,
                        userName = note.userName,
                        location = "${note.latitude},${note.longitude}",
                        text = note.text
                    )
                }
                ViewState.Data(items)
            }
            is Result.Failure -> ViewState.Failure(error)
        }

    data class Item(
        val id: String,
        val userName: String,
        val location: String,
        val text: String
    )
}