package com.tigerspike.landmark.data.datasource

import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tigerspike.landmark.data.mapper.resultCatching
import com.tigerspike.landmark.domain.model.Result
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Gustavo Enriquez on 26/7/20.
 **/

class UserRemoteDataSource @Inject constructor() {

    fun getUser() = Firebase.auth.currentUser

    suspend fun signIn(email: String, password: String): Result<Unit> = resultCatching {
        val signIpResult = suspendCoroutine<Boolean> { continuation ->
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        if (signIpResult) Unit else throw Exception()
    }

    suspend fun signUp(name: String, email: String, password: String): Result<Unit> = resultCatching {
        // Create user account user email and password
        val signUpResult = suspendCoroutine<Boolean> { continuation ->
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        // Add user name before creating account
        val nameResult = suspendCoroutine<Boolean> { continuation ->
            val updateRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            Firebase.auth.currentUser?.updateProfile(updateRequest)
                ?.addOnSuccessListener {
                    continuation.resume(true)
                }
                ?.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        if (signUpResult && nameResult) Unit else throw Exception()
    }

    fun signOut(): Result<Unit> = resultCatching {
        Firebase.auth.signOut()
    }

}