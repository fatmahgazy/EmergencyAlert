package org.codeforegypt.quickassestant.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.codeforegypt.quickassestant.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM User")
    fun getUser(): User

    @Query("SELECT * FROM User")
    fun getUsers(): List<User>

    @Query("DELETE FROM User")
    fun deleteAllUsers()
}