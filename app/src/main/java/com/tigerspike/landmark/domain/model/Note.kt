package com.tigerspike.landmark.domain.model

/**
 * Created by Gustavo Enriquez on 25/7/20.
 **/

data class Note(
    val id: String,
    val text: String,
    val latitude: Double,
    val longitude: Double,
    val userName: String,
    val userId:String
)