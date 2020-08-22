package com.tigerspike.landmark.data.datasource

import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tigerspike.landmark.data.mapper.resultCatching
import com.tigerspike.landmark.data.mapper.toMap
import com.tigerspike.landmark.data.mapper.toNote
import com.tigerspike.landmark.domain.model.Note
import com.tigerspike.landmark.domain.model.Result
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

@Singleton
class NoteRemoteDataSource @Inject constructor() {

    suspend fun fetchNotes(): Result<List<Note>> = resultCatching {
        suspendCoroutine { continuation ->
            Firebase.firestore.collection("notes").get()
                .addOnSuccessListener { result ->
                    val notes = result.map { it.toNote() }
                    continuation.resume(notes)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    suspend fun fetchNotesByTextOrUserName(query: String): Result<List<Note>> = resultCatching {
        suspendCoroutine { continuation ->
            // Getting the list from cache due to performance issues
            Firebase.firestore.collection("notes").get(Source.CACHE)
                .addOnSuccessListener { result ->
                    val notes = result
                        .map { it.toNote() }
                        .filter { it.text.contains(query, true) || it.userName.contains(query, true) }
                    continuation.resume(notes)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    suspend fun postNote(note: Note): Result<Unit> = resultCatching {
        suspendCoroutine<Unit> { continuation ->
            Firebase.firestore.collection("notes").add(note.toMap())
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }
}