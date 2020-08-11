package com.tigerspike.landmark.domain.useCase

import com.tigerspike.landmark.domain.contract.NoteRepository
import com.tigerspike.landmark.domain.contract.UserRepository
import com.tigerspike.landmark.domain.model.Error
import com.tigerspike.landmark.domain.model.Note
import com.tigerspike.landmark.domain.model.Result
import com.tigerspike.landmark.domain.model.User
import javax.inject.Inject

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

class SaveNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(text: String, latitude: Double, longitude: Double): Result<Unit> {
        return when (val result = userRepository.getUser()) {
            is Result.Success -> {

                when (val user = result.data) {
                    is User.Authenticated -> {
                        val note = Note(
                            id = "",
                            text = text,
                            latitude = latitude,
                            longitude = longitude,
                            userName = user.name,
                            userId = user.id
                        )
                        noteRepository.saveNote(note)
                    }
                    User.Guest -> Result.Failure(Error.DomainError(Exception()))
                }
            }
            is Result.Failure -> Result.Failure(result.error)
        }
    }
}