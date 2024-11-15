package com.example.e_chitii.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.e_chitii.LCViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_chitii.CommonImage
import com.example.e_chitii.data.Message

//@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChatScreen(
    navController: NavController, vm: LCViewModel,
    chatId: String = "1234"
) {
    var reply by rememberSaveable {
        mutableStateOf("")
    }
    val onSendReply = {
        vm.onSendReply(chatId, reply)
        reply = ""
    }
    val myUser = vm.userData.value
    val currentChat = vm.chats.value.first(){
        it.chatId == chatId
    }
    val chatUser = if(myUser?.userId == currentChat.user2?.userId) currentChat.user1 else currentChat.user2

    LaunchedEffect(key1 = Unit) {
        vm.populateChatMessages(chatId)
    }
    BackHandler {
        vm.dePopulateChatMessages()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = Color.Red
                ),
                title = {
                    ChatHeader(
                        name = chatUser?.name.toString(),
                        imageUrl = chatUser?.imageUrl.toString(),
                        onBackClicked = {navController.popBackStack()
                            vm.dePopulateChatMessages()} )

                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                MessageBox(
                    modifier = Modifier.weight(90f),
                    chatMessages = vm.chatMessages.value,
                    currentUserId = myUser?.userId ?: "No user"
                )


                ReplyBox(
                    modifier = Modifier.weight(10f),
                    reply = reply,
                    onReplyChange = { reply = it },
                    onSendClick = { onSendReply() })

            }
        }
    )

}

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    chatMessages: List<Message>,
    currentUserId: String
) {

    LazyColumn(modifier = modifier) {
        items(chatMessages) { msg ->
            val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = if (msg.sendBy == currentUserId) Color.Green else Color.Blue

            Column(modifier = Modifier.fillMaxWidth().padding(4.dp), horizontalAlignment = alignment) {
                Box(
                    modifier = Modifier
                        .background(color, shape = RoundedCornerShape(12.dp)) // Background color
                        .padding(12.dp),
                    // Padding inside the box
                ) {
                    Text(
                        text = msg.message ?: "No message available",
                        color = Color.White // Text color
                    )
                }
            }

        }
    }
}

@Composable
fun ChatHeader(
    name: String,
    imageUrl: String,
    onBackClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "Back to chatList",
            modifier = Modifier
                .padding(4.dp)
                .clickable {
                    onBackClicked()
                }
        )

        // Profile Image (If you want to show an image)
        CommonImage(
            data = imageUrl, // Replace with actual image URL
            modifier = Modifier
                .padding(start = 20.dp)
                .size(50.dp)
                .clip(CircleShape)
        )

        // Chat Name
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 16.sp),
            modifier = Modifier.padding(start = 10.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ReplyBox(modifier: Modifier,reply: String, onReplyChange: (String) -> Unit, onSendClick: () -> Unit) {
    Row(
        modifier = Modifier
            .imePadding()
            .windowInsetsPadding(WindowInsets(bottom = 0))
            .padding(bottom = 15.dp, start = 10.dp)
            .fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        TextField(
            value = reply,
            onValueChange = { onReplyChange(it) },
            maxLines = 3,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.Red
            )
        )

        Button(onClick = { onSendClick() }) {
            Text(text = "Send")
        }
    }

}
