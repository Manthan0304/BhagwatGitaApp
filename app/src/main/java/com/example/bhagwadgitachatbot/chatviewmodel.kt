package com.example.bhagwadgitachatbot

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class chatviewmodel : ViewModel() {
    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()

    private val _state = MutableStateFlow(UserData(
        email = "",
        userid = "",
        username = ""
    ))
    val state = _state.asStateFlow()

    private val userCollection = FirebaseFirestore.getInstance().collection("users")
    private var userDataListener: ListenerRegistration? = null

    fun getUserData(userid: String) {
        userDataListener = userCollection.document(userid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching user data", error)
                    return@addSnapshotListener
                }
                
                if (value != null && value.exists()) {
                    val username = value.getString("username") ?: ""
                    val email = value.getString("email") ?: ""
                    _state.value = UserData(
                        username = username,
                        email = email,
                        userid = userid
                    )
                }
            }
    }

    fun updateSignInState(signedIn: Boolean) {
        _isSignedIn.value = signedIn
    }

    fun resetState() {
        _isSignedIn.value = false
        _state.value = UserData(email = "", userid = "", username = "")
    }

    override fun onCleared() {
        userDataListener?.remove()
        super.onCleared()
    }
}