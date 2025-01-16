package com.rudy.contactapp.view.contactDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.rudy.contactapp.Model.model.Contact
import com.rudy.contactapp.ui.theme.Purple40
import com.rudy.contactapp.viewModel.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsScreen(
    contact:Contact,
    viewModel: ContactViewModel,
    navController: NavController
) {
    val showDialog = rememberSaveable {mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(
                    onClick =  { showDialog.value = true },
                    containerColor = Purple40
                ) {
                    Icon(Icons.Default.Delete, "Delete Contact", tint = Color.White)
                }
                FloatingActionButton(
                    onClick = {navController.navigate("editContactScreen/${contact.id}")},
                    containerColor = Purple40
                ) {
                    Icon(Icons.Default.Edit, "Edit Contact", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image

            if (showDialog.value) {
                DeleteConfirmationDialog(
                    contact = contact,
                    onDeleteConfirmed = {
                        viewModel.deleteContact(contact)
                        navController.navigate("contactScreen")
                    },
                    onDismiss = { showDialog.value = false }
                )
            }

            Image(
                painter = rememberAsyncImagePainter(contact.image),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Details
            Card(modifier = Modifier.fillMaxWidth()
                .padding(4.dp),
                colors = CardDefaults.cardColors(Color.White),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {

                ContactDetailItem(
                    icon = Icons.Default.Person,
                    text = ("${contact.firstName} ${contact.lastName}")
                )

                Spacer(modifier = Modifier.height(16.dp))

                ContactDetailItem(
                    icon = Icons.Default.Phone,
                    text = (contact.number.toString())
                )

                Spacer(modifier = Modifier.height(16.dp))

                ContactDetailItem(
                    icon = Icons.Default.Email,
                    text = (contact.email)
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    contact: Contact,
    onDeleteConfirmed: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Contact") },
        text = {
            Text(text = "Are you sure you want to delete ${contact.firstName} ${contact.lastName}? This action cannot be undone.")
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = {
                    onDeleteConfirmed()
                    onDismiss() // Close the dialog after confirming
                }
            ) {
                Text("Delete", color = Color.Red)
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ContactDetailItem(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

