package com.rudy.contactapp.data.dataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rudy.contactapp.Model.model.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        const val DB_NAME = "contacts_db"
    }
}
