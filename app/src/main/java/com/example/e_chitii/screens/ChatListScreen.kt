package com.example.e_chitii.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.e_chitii.CommonDivider
import com.example.e_chitii.CommonProgressBar
import com.example.e_chitii.CommonRow
import com.example.e_chitii.DestinationScreen
import com.example.e_chitii.LCViewModel
import com.example.e_chitii.Titletext
import com.example.e_chitii.navigateTo
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navController: NavController, vm: LCViewModel
) {
    val inProgress = vm.inProgressChat
    if (inProgress.value) {
        CommonProgressBar()
    } else {
        val chats = vm.chats.value
        val userData = vm.userData.value
        val showDialog = remember {
            mutableStateOf(false)
        }
        val onFabClicked: () -> Unit = { showDialog.value = true }
        val onDismiss: () -> Unit = { showDialog.value = false }
        val onAddChat: (String) -> Unit = {
            vm.addChat(it)
            showDialog.value = false
        }
        Scaffold(floatingActionButton = {
            FAB(
                showDialog = showDialog.value,
                onFabClicked = onFabClicked,
                onDismiss = onDismiss,
                onAddChat = onAddChat,
            )
        }, bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(90.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                BottonNavigationMenu(
                    selectedItem = BottomNavigationItem.CHATLIST,
                    navController = navController,
                    modifier = Modifier
                )
            }
        },topBar = {
            androidx.compose.material3.TopAppBar(
                modifier = Modifier.height(90.dp),
                colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                title = { Text(text = "Chats", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Color.Blue)) })
        },
            content = { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(93f)
                ) {
                    if (chats.isEmpty()) {
                        Column(
                            modifier =
                            Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "No chats")

                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(chats) { chat ->
                                val chatUser = if (chat.user1?.userId == userData?.userId) {
                                    chat.user2
                                } else {
                                    chat.user1
                                }
                                CommonRow(
                                    imageUrl = chatUser?.imageUrl.toString(),
                                    name = chatUser?.name.toString()
                                ) {


                                    navigateTo(
                                        navController = navController,
                                        DestinationScreen.SingleChat.createRoute(chat.chatId.toString())
                                    )
                                }
                            }

                        }
                    }
                }

            }
        })

    }
}

@Composable
fun FAB(
    showDialog: Boolean,
    onFabClicked: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = {
            onDismiss()
            addChatNumber.value = ""
        }, confirmButton = {
            Button(onClick = { onAddChat(addChatNumber.value) }) {
                Text(text = "Add Chat")
            }
        },
            title = { Text(text = "Add Chat") },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value,
                    onValueChange = { addChatNumber.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )
    }
    FloatingActionButton(onClick = { onFabClicked() },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        modifier = Modifier.padding(bottom = 70.dp, end = 10.dp)) {

        Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)
    }
}

