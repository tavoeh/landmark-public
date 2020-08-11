package com.tigerspike.landmark.domain.model

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

sealed class User {
    object Guest : User()
    data class Authenticated(
        val id: String,
        val name: String
    ) : User()
}