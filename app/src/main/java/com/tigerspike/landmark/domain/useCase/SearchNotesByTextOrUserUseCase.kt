package com.tigerspike.landmark.domain.useCase

import com.tigerspike.landmark.domain.contract.NoteRepository
import com.tigerspike.landmark.domain.model.Note
import com.tigerspike.landmark.domain.model.Result
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

class SearchNotesByTextOrUserUseCase @Inject constructor(private val repository: NoteRepository) {

    suspend fun execute(query: String): Result<List<Note>> {
        return repository.getNotesByTextOrUserName(query)
    }
}