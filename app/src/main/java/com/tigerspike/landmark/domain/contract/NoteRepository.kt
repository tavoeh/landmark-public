package com.tigerspike.landmark.domain.contract

import com.tigerspike.landmark.domain.model.Note
import com.tigerspike.landmark.domain.model.Result

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 * NoteRepository domain contract
 **/

interface NoteRepository {
    suspend fun getNotes(): Result<List<Note>>
    suspend fun getNotesByTextOrUserName(query: String): Result<List<Note>>
    suspend fun saveNote(note: Note): Result<Unit>
}