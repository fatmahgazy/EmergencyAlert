package org.codeforegypt.quickassestant.domain.repository

import org.codeforegypt.quickassestant.data.model.ModifyContact
import org.codeforegypt.quickassestant.data.model.Contact

interface IContactRepo {

    suspend fun addContact(contact: ModifyContact): Boolean

    suspend fun getContacts(): List<Contact>

    suspend fun deleteContact(contact: Contact): Boolean
}