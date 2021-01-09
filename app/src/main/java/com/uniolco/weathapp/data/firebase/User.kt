package com.uniolco.weathapp.data.firebase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val login: String = "",
    @ColumnInfo(name = "email")
    val email: String = "",
    val phoneNumber: String = "",
    val name: String = "",
    val surname: String = "",
    val address: String = ""
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
