package org.codeforegypt.quickassestant.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.codeforegypt.quickassestant.data.model.Contact

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContacts(contacts: List<Contact>)

    @Query("SELECT * FROM Contact")
    fun getAllContacts(): List<Contact>


    @Query("DELETE FROM Contact")
    fun deleteAllContacts()


    @Delete
    fun deleteContact(contact: Contact)
}