package com.example.bhagwadgitachatbot

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException


class googleauthuiclient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    val viewmodel: chatviewmodel,

    ) {

    private val auth = Firebase.auth
    private val firestore = FirebaseFirestore.getInstance()

    private fun saveUserToFirestore(username: String) {
        val userId = auth.currentUser?.uid ?: return
        val email = auth.currentUser?.email ?: ""
        
        val userMap = hashMapOf(
            "username" to username,
            "userid" to userId,
            "email" to email
        )

        firestore.collection("users").document(userId)
            .set(userMap)
            .addOnSuccessListener { Log.d("Firestore", "User added successfully") }
            .addOnFailureListener { e -> Log.e("Firestore", "Error adding user", e) }
    }

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildsignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    private fun buildsignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("269258198486-1qin5vjpf68cj64t00u0u0nkcfubgfq2.apps.googleusercontent.com")
                .build()
        ).setAutoSelectEnabled(true).build()
    }
    suspend fun signinwithintent(intent: Intent): Boolean {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await()
            // Save user to Firestore after successful sign in
            user.user?.displayName?.let { username ->
                saveUserToFirestore(username)
            }
            viewmodel.updateSignInState(true)
            true
        } catch(e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getSignedInUser(): FirebaseUser? {
        return auth.currentUser
    }
}