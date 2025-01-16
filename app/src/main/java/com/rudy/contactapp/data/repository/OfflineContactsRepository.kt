package com.rudy.contactapp.data.repository

import com.rudy.contactapp.Model.model.Contact
import com.rudy.contactapp.data.dataSource.ContactDao
import kotlinx.coroutines.flow.Flow

class OfflineContactsRepository(private val contactDao: ContactDao) {
   val allContacts : Flow<List<Contact>> = contactDao.getAllContacts()


    suspend fun insertContact(contact: Contact) = contactDao.insert(contact)

     suspend fun deleteContact(contact: Contact) = contactDao.delete(contact)

    suspend fun updateContact(contact: Contact) = contactDao.update(contact)
}
