package com.uniolco.weathapp.data.firebase

data class User(
    val login: String,
    val email: String,
    val phoneNumber: String,
    val name: String,
    val surname: String,
    val address: String
)
