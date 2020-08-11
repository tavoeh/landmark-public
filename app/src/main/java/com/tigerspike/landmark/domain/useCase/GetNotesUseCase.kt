package com.tigerspike.landmark.domain.useCase

import com.tigerspike.landmark.domain.contract.NoteRepository
import com.tigerspike.landmark.domain.contract.UserRepository
import com.tigerspike.landmark.domain.model.Note
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.model.User
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute(): Result<Pair<List<Note>, List<Note>>> {

        // Get user id to filter the notes
        val userResult = userRepository.getUser()
        val userId: String? = if (userResult is Result.Success) {
            when (val user = userResult.data) {
                is User.Authenticated -> user.id
                User.Guest -> null
            }
        } else null

        return when (val notesResult = noteRepository.getNotes()) {
            is Result.Success -> {

                // Filter note list if user id exists
                val notes = notesResult.data
                val userNotes = userId?.let { notes.filter { note -> note.userId == it } } ?: listOf()
                val otherNotes = (userId?.let { notes.subtract(userNotes) } ?: notes).toList()

                Result.Success(userNotes to otherNotes)

            }
            is Result.Failure -> Result.Failure(notesResult.error)
        }
    }
}