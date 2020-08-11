package com.tigerspike.landmark.util

import com.tigerspike.landmark.domain.model.Note
import org.mockito.Mockito

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

val mockNote = Note("", "", 0.0, 0.0, "User", "1")

val mockNotes = listOf(
    mockNote.copy(id = "1", text = "First note", userName = "Gustavo"),
    mockNote.copy(id = "2", text = "Second note", userName = "Gustavo"),
    mockNote.copy(id = "3", text = "Third note", userName = "Gustavo")
)

/**
 * Returns Mockito.any() as nullable type to avoid java.lang.IllegalStateException when
 * null is returned.
 */
fun <T> any(): T = Mockito.any<T>()
