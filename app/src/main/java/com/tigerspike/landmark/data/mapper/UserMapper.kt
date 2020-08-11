package com.tigerspike.landmark.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.tigerspike.landmark.domain.model.User

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

fun FirebaseUser.toUser() = User.Authenticated(
    id = uid,
    name = displayName ?: "Anonymous"
)