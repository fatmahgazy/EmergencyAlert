package org.codeforegypt.quickassestant.data.repository

import org.codeforegypt.quickassestant.api.ContactsApi
import org.codeforegypt.quickassestant.data.db.ContactDao
import org.codeforegypt.quickassestant.data.db.UserDao
import org.codeforegypt.quickassestant.data.model.ModifyContact
import org.codeforegypt.quickassestant.data.model.Contact
import org.codeforegypt.quickassestant.domain.repository.IContactRepo
import java.io.IOException
import javax.inject.Inject

class ContactRepo @Inject constructor(
    private val contactsApi: ContactsApi,
    private val userDao: UserDao,
    private val contactDao: ContactDao
): IContactRepo {

    override suspend fun addContact(contact: ModifyContact): Boolean {
        return try {
            val email = userDao.getUser().email
            val updatedContact = contact.copy(email = email)
            val response = contactsApi.addContact(
                updatedContact.name,
                updatedContact.phone,
                updatedContact.email
            ).await()

            return response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getContacts(): List<Contact> {
        return try {
            val email = userDao.getUser().email
            val contacts = contactsApi.getContacts(email).await()

            return if (contacts.isSuccessful && contacts.body() != null) {
                contactDao.deleteAllContacts()
                contactDao.insertContacts(contacts.body()!!.contacts)
                contacts.body()!!.contacts
            } else
                throw NullPointerException()
        } catch (e: IOException) {
            contactDao.getAllContacts()
        }
    }

    override suspend fun deleteContact(contact: Contact): Boolean {
        return try {
            val email = userDao.getUser().email
            val modifyContact = ModifyContact(
                contact.name,
                contact.phone,
                email
            )
            val isDeletionSuccessful = contactsApi.deleteContact(modifyContact.name, modifyContact.phone, modifyContact.email).await().isSuccessful
            if (isDeletionSuccessful)
                contactDao.deleteContact(contact)
            return isDeletionSuccessful
        } catch (e: Exception) {
            false
        }
    }

}