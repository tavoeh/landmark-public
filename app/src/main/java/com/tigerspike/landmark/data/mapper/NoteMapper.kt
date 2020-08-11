package com.tigerspike.landmark.data.mapper

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.tigerspike.landmark.domain.model.Note

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

fun QueryDocumentSnapshot.toNote() = Note(
    id = id,
    text = data["text"] as String,
    latitude = data["latitude"] as Double,
    longitude = data["longitude"] as Double,
    userName = data["userName"] as String,
    userId = data["userId"] as String
)

fun Note.toMap() = hashMapOf(
    "text" to text,
    "latitude" to latitude,
    "longitude" to longitude,
    "userName" to userName,
    "userId" to userId
)