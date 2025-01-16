package com.rudy.contactapp.view.contactList

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.rudy.contactapp.Model.model.Contact
import com.rudy.contactapp.ui.theme.Purple40
import com.rudy.contactapp.viewModel.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    contactViewModel: ContactViewModel,
    navController: NavController
) {
    val contacts by contactViewModel.allContact.observeAsState(emptyList())
    val context = LocalContext.current.applicationContext


    // Request CALL_PHONE permission
    val callPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission is required to make phone calls.", Toast.LENGTH_SHORT).show()
        }
    }

    // Check and request permission when the composable is loaded
    LaunchedEffect(Unit) {
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CALL_PHONE
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            callPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addContactScreen") },
                containerColor = Purple40
            ) {
                Icon(Icons.Default.Add, "Add Contact", tint = Color.White)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(contacts) { contact ->
                ContactListItem(
                    contact = contact,
                    onClick = {navController.navigate("contactDetailScreen/${contact.id}")  }
                )
            }
        }
    }
}

@Composable
private fun ContactListItem(
    contact: Contact,
    onClick: () -> Unit
) {
    val context = LocalContext.current


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(12.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contact Image
            Image(
                painter = rememberAsyncImagePainter(contact.image),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Contact Name
            Text(
                text = "${contact.firstName} ${contact.lastName}",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            // Action Icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Call Icon
                IconButton(onClick = {
                    if (contact.number.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_CALL).apply {
                            data = Uri.parse("tel:${contact.number}")
                        }
                        if (context.packageManager.resolveActivity(intent, 0) != null) {
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Unable to make the call.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Phone number is empty.", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "Call",
                        tint = Purple40
                    )
                }
            }
        }
    }
}



