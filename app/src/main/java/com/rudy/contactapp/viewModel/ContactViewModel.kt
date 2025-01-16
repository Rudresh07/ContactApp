package com.rudy.contactapp.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rudy.contactapp.Model.model.Contact
import com.rudy.contactapp.data.repository.OfflineContactsRepository
import kotlinx.coroutines.launch

class ContactViewModel(
    private val repository: OfflineContactsRepository,
    private val savedStateHandle: SavedStateHandle // Added SavedStateHandle for state persistence
) : ViewModel() {


    private val _image = MutableLiveData<Uri?>()
    val image: LiveData<Uri?> = _image

    private val _firstName = MutableLiveData<String>()
    val firstName : LiveData<String> = _firstName

    private val _lastName = MutableLiveData<String>()
    val lastName : LiveData<String> = _lastName

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber : LiveData<String> = _phoneNumber

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    fun onImageUpdate(newImg : Uri?){
        _image.value = newImg
    }

    fun onFirstNameUpdate(newFirstName:String){
        _firstName.value = newFirstName
    }
    fun onLastNameUpdate(newLastName:String){
        _lastName.value = newLastName
    }
    fun onPhoneNumberUpdate(newPhoneNumber: String){
        _phoneNumber.value = newPhoneNumber
    }
    fun onEmailUpdate(newEmail:String){
        _email.value = newEmail
    }

    // LiveData for all contacts
    val allContact: LiveData<List<Contact>> = repository.allContacts.asLiveData()

    fun addContact(image:String,firstName:String,lastName:String,phoneNumber:String,email:String){
        val contact = Contact(
            0, image = image, firstName = firstName, lastName = lastName, number = phoneNumber, email = email
        )
        viewModelScope.launch {
            repository.insertContact(contact)
        }
    }

    // Update an existing contact
    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.updateContact(contact)
        }
    }

    // Delete a contact
    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    // Reset form fields (for Add/Edit screen)
    fun resetFields() {
            _firstName.value = ""
            _lastName.value = ""
            _phoneNumber.value = ""
            _email.value = ""
            _image.value = null // Reset image as well if needed
        }

}

// ViewModelFactory to initialize ViewModel
class ContactViewModelFactory(
    private val repository: OfflineContactsRepository,
    private val savedStateHandle: SavedStateHandle // Pass SavedStateHandle here
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactViewModel(repository, savedStateHandle) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
