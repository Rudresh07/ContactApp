package com.rudy.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.rudy.contactapp.data.dataSource.ContactsDatabase
import com.rudy.contactapp.data.repository.OfflineContactsRepository
import com.rudy.contactapp.view.addEditContact.AddContactScreen
import com.rudy.contactapp.view.contactDetails.ContactDetailsScreen
import com.rudy.contactapp.view.contactList.ContactsScreen
import com.rudy.contactapp.view.editContact.EditContactScreen
import com.rudy.contactapp.viewModel.ContactViewModel
import com.rudy.contactapp.viewModel.ContactViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database and repository
        val database =
            Room.databaseBuilder(applicationContext, ContactsDatabase::class.java, "contacts_db")
                .build()
        val repository = OfflineContactsRepository(database.contactDao())

        // Initialize the ViewModel using the repository
        val viewModel: ContactViewModel by viewModels {
            ContactViewModelFactory(repository, savedStateHandle = SavedStateHandle()) // Pass an empty SavedStateHandle if no saved instance state
        }

        setContent {
           val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "contactScreen" ){
                composable("contactScreen"){ ContactsScreen(viewModel,navController) }
                composable("addContactScreen"){ AddContactScreen(viewModel,navController)}
                composable("contactDetailScreen/{contactId}"){ backStackEntry ->
                    val contactId = backStackEntry.arguments?.getString("contactId")?.toInt()
                    val contact = viewModel.allContact.observeAsState(initial = emptyList()).value.find { it.id == contactId }

                    contact?.let { ContactDetailsScreen(contact,viewModel,navController) }
                }
                composable("editContactScreen/{contactId}"){ backStackEntry ->
                    val contactId = backStackEntry.arguments?.getString("contactId")?.toInt()
                    val contact = viewModel.allContact.observeAsState(initial = emptyList()).value.find { it.id == contactId }

                    contact?.let { EditContactScreen(contact,viewModel,navController) }
                }
            }
        }
    }
}
