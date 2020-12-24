package com.uniolco.weathapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uniolco.weathapp.data.firebase.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateUser(user: User)

    @Query("SELECT * FROM user_table WHERE email LIKE :emal")
    fun getUser(emal: String): LiveData<User>
}