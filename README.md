# Contact App
The Contact App allows users to efficiently manage contacts, including adding, editing, and deleting contacts. Additionally, it features the ability to make phone calls and send emails directly from the app. The app incorporates Jetpack Compose for UI design, Room for local database management, and modern state management techniques.

# Features
1. Add, Edit, Delete Contacts
Add new contacts with essential details like name, phone number, and email.
Edit and update contact details.
Delete contacts with a confirmation dialog.
2. Make Calls
Directly make phone calls to contacts with a stored phone number.
3. State Management with ViewModel
Utilizes ViewModel for managing UI-related data in a lifecycle-conscious way.
Ensures proper state management when adding, editing, or deleting contacts.
4. User-Friendly UI
Modern UI designed using Jetpack Compose.
Responsive layouts with proper alignment, padding, and spacing.
5. Offline Storage
Data is stored locally using the Room database, ensuring availability even when offline.
Real-time data synchronization ensures updates are immediately reflected in the UI.
# Technologies Used
Jetpack Compose: UI framework for building the interface in a declarative way.
Room Database: For offline data storage and persistence.
ViewModel: To manage UI-related data and ensure proper state handling.
Toast Notifications: User feedback provided through toasts (e.g., when a contact is added or deleted).
# Getting Started
To run this app locally, follow these steps:

## Prerequisites
Install Android Studio.
Basic knowledge of Kotlin and Android development.
## Steps
Clone the repository:

git clone https://github.com/Rudresh07/ContactApp.git

Open the project in Android Studio.

Run the app on an emulator or a physical device.

The app will request necessary permissions for making calls and sending emails.

# Permissions
CALL_PERMISSION: Required to make phone calls.
Make sure to handle permission requests properly, especially when performing actions like calling or emailing.

State Management
The app uses ViewModel to store and manage UI-related data, ensuring that the UI reacts to data changes like adding or deleting a contact.
