package com.example.e_chitii

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.collection.emptyLongSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.e_chitii.data.CHATS
import com.example.e_chitii.data.ChatData
import com.example.e_chitii.data.ChatUser
import com.example.e_chitii.data.Events
import com.example.e_chitii.data.MESSAGE
import com.example.e_chitii.data.Message
import com.example.e_chitii.data.Status
import com.example.e_chitii.data.USER_NODE
import com.example.e_chitii.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Exception
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db: FirebaseFirestore,
    var storage: FirebaseStorage) : ViewModel() {
    val inProgress = mutableStateOf(false)
    val inProgressChat = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Events<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    var chats = mutableStateOf<List<ChatData>>(listOf())
    val chatMessages = mutableStateOf<List<Message>>(listOf())
    val inProgressChatMessages = mutableStateOf(false)
    var currentChatMessageListener: ListenerRegistration? = null
    val status = mutableStateOf<List<Status>>(listOf())
    val inProgressStatus = mutableStateOf(false)
    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser!=null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun populateChatMessages(chatId: String){
         inProgressChatMessages.value = true
        currentChatMessageListener = db.collection(CHATS).document(chatId).collection(MESSAGE).addSnapshotListener(){
            value,error ->
            if (error!=null){
                handleException(error)
                inProgressChatMessages.value = false
            }
            if (value!=null){
                chatMessages.value = value.documents.mapNotNull {
                    it.toObject<Message>()
                }.sortedBy{it.timeStamp}
                inProgressChatMessages.value = false
            }
        }
    }

    fun dePopulateChatMessages(){
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }

    fun populateChats(){
        inProgressChat.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId)
            )
        ).addSnapshotListener{
            value,error ->
            if (error!=null){
                handleException(error,"Populate chats error")
                inProgressChat.value = false
            }
            if (value!=null){
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inProgressChat.value = false
            }

        }

    }

    fun onSendReply(
        chatId: String,
        message: String,
    ){
        val time = Calendar.getInstance().time.toString()
        val msg = Message(userData.value?.userId,message,time)
        db.collection(CHATS).document(chatId).collection(MESSAGE).document().set(msg)
    }


    fun signUp(name: String, number: String, email: String, password: String) {
        inProgress.value = true

        // Use logical OR (||) and validate input
        if (name.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty()) {
            handleException(costumMessage = "Please fill in all fields")
            inProgress.value = false
            return
        }


        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                // Create user with Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                         signIn.value = true
                        inProgress.value = false
                        createOrUpdateProfile(name, number) // Call to create the user profile
                    } else {
                        // Handle sign-up error and stop loading state
                        handleException(task.exception, "Sign up error")
                        inProgress.value = false
                    }
                }
            } else {
                // Handle case where user with this number already exists
                handleException(costumMessage = "User with this number already exists")
                inProgress.value = false
            }
        }.addOnFailureListener {
            // Handle Firestore query error
            handleException(it, "Error checking number existence")
            inProgress.value = false
        }
    }

    fun logIn(email: String,password: String){
        if (email.isEmpty() || password.isEmpty()){
            handleException(costumMessage = "Put values in it")
        }
        else{
            inProgress.value = true
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    signIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let{
                        getUserData(it)
                    }
                }else{
                    handleException(it.exception,"Log in error")
                }
            }
        }
    }

    fun uploadProfileImage(uri: Uri){
        uploadImage(uri){data ->
             createOrUpdateProfile(imageUrl = data.toString())
        }

    }

    fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit){
        inProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            inProgress.value = false
        }.addOnFailureListener{
            handleException(it,"Image upload error")
        }
    }
    fun createOrUpdateProfile(name: String? = null, number: String? = null, imageUrl: String? = null) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageUrl ?: userData.value?.imageUrl,
        )

        uid?.let {
            inProgress.value = true

            db.collection(USER_NODE).document(uid).get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val updates = mutableMapOf<String, Any?>()

                    name?.let {
                        updates["name"] = it
                    }
                    number?.let {
                        updates["number"] = it
                    }
                    imageUrl?.let {
                        updates["imageUrl"] = it
                    }

                    db.collection(USER_NODE).document(uid)
                        .update(updates)
                        .addOnSuccessListener {
                            inProgress.value = false
                            getUserData(uid)
                        }.addOnFailureListener { exception ->
                            inProgress.value = false
                            handleException(exception, "Failed to update profile")
                        }
                } else {
                    // Create new user data if no document exists
                    db.collection(USER_NODE).document(uid).set(userData)
                    inProgress.value = false
                    getUserData(uid)
                }
            }.addOnFailureListener { exception ->
                handleException(exception, "Create or update profile error")
            }
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener{value,error ->
            if (error!=null){
                handleException(error,"Get user data error")
            }
            if (value!=null){
                val user = value.toObject<UserData>()
                userData.value = user
                inProgress.value = false
                populateChats()
            }
        }
    }

    fun handleException(exception: Exception? = null, costumMessage: String = ""){
        Log.e("Exception", "Live chat exception",exception)
        exception?.printStackTrace()

        val errorMsg = exception?.localizedMessage
        val message = if(costumMessage.isNullOrEmpty()) errorMsg else costumMessage
//        Toast.makeText(requireContext(requireContext()), message, Toast.LENGTH_LONG).show()
        eventMutableState.value = Events(message.toString())
        inProgress.value = false
    }

    fun onLogOut() {
        auth.signOut()
        signIn.value = false
        userData.value = null
        eventMutableState.value = Events("Logged out")
    }

    fun addChat(number: String) {
        if (number.isEmpty() || !number.isDigitsOnly()) {
            handleException(costumMessage = "Please enter a valid number")
        } else {
            db.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)
                    )
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                handleException(costumMessage = "User with this number does not exist")
                            } else {
                                val chatPartners = it.toObjects(UserData::class.java)[0]
                                val id = db.collection(CHATS).document().id
                                val chat = ChatData(
                                    chatId = id,
                                    user1 = ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.number,
                                        userData.value?.imageUrl
                                    ),
                                    user2 = ChatUser(
                                        chatPartners.userId,
                                        chatPartners.name,
                                        chatPartners.number,
                                        chatPartners.imageUrl
                                    )
                                )
                                db.collection(CHATS).document(id).set(chat)
                            }
                        }.addOnFailureListener{
                            handleException(it,"Add chat error")
                        }
                } else {
                    handleException(costumMessage = "Chat already exists")
                }

            }

        }
    }
}

