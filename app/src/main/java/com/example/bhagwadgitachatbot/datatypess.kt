package com.example.bhagwadgitachatbot

data class Signinresult(
    val data : UserData?,
    val errorMessage : String?
)
data class UserData(
    val email : String?,
    val userid : String?,
)
