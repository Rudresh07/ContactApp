package com.rudy.contactapp.view.editContact

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.rudy.contactapp.Model.model.Contact
import com.rudy.contactapp.ui.theme.Purple40
import com.rudy.contactapp.utils.copyUriToInternalStorage
import com.rudy.contactapp.viewModel.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactScreen(
    contact: Contact,
    viewModel: ContactViewModel,
    navController: NavController
) {
    val context= LocalContext.current
    val imageUri by viewModel.image.observeAsState(contact.image)
    val firstName by viewModel.firstName.observeAsState(contact.firstName)
    val lastName by viewModel.lastName.observeAsState(contact.lastName)
    val phoneNumber by viewModel.phoneNumber.observeAsState(contact.number)
    val emailAddress by viewModel.email.observeAsState(contact.email)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri:Uri? ->
        uri?.let {
            val imagePath = copyUriToInternalStorage(context,it,"$firstName.jpg")
            imagePath?.let { path ->  viewModel.onImageUpdate(Uri.parse(path)) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Contact", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40
                )
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Camera Icon

                Image(painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop)


            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {launcher.launch("image/*")},
                colors = ButtonDefaults.buttonColors(Purple40)
            ){
                Text("Choose Image")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Contact Form Fields
            ContactTextField(
                value = firstName,
                onValueChange = { viewModel.onFirstNameUpdate(it) },
                label = "First Name",
                leadingIcon = { Icon(Icons.Default.Person, "Person") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactTextField(
                value = lastName,
                onValueChange = { viewModel.onLastNameUpdate(it) },
                label = "Last Name",
                leadingIcon = { Icon(Icons.Default.Person, "Person") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactTextField(
                value = phoneNumber,
                onValueChange = { it.also {viewModel.onPhoneNumberUpdate(it)} },
                label = "Phone Number",
                keyboardType = KeyboardType.Phone,
                leadingIcon = { Icon(Icons.Default.Phone, "Phone") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactTextField(
                value = emailAddress,
                onValueChange = {viewModel.onEmailUpdate(it) },
                label = "Email Address",
                keyboardType = KeyboardType.Email,
                leadingIcon = { Icon(Icons.Default.Email, "Email") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        val updateContact = contact.copy(image = imageUri.toString(), firstName = firstName, lastName = lastName, number = phoneNumber, email = emailAddress)
                        viewModel.updateContact(updateContact)
                        navController.navigate("contactScreen"){
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple40
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("SAVE")
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
            .height(56.dp),
        singleLine = true
    )
}


