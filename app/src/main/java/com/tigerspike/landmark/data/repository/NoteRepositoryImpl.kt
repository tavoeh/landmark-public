package com.tigerspike.landmark.data.repository

import com.tigerspike.landmark.data.datasource.NoteRemoteDataSource
import com.tigerspike.landmark.domain.contract.NoteRepository
import com.tigerspike.landmark.domain.model.Note
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.util.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 25/7/20.
 *
 * NoteRepository implementation according to the Domain contract
 **/

class NoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: NoteRemoteDataSource,
    private val dispatchers: DispatcherProvider
) : NoteRepository {

    override suspend fun getNotes(): Result<List<Note>> =
        withContext(dispatchers.io()) {
            remoteDataSource.fetchNotes()
        }

    override suspend fun getNotesByTextOrUserName(query: String): Result<List<Note>> =
        withContext(dispatchers.io()) {
            remoteDataSource.fetchNotesByTextOrUserName(query)
        }

    override suspend fun saveNote(note: Note): Result<Unit> =
        withContext(dispatchers.io()) {
            remoteDataSource.postNote(note)
        }
}