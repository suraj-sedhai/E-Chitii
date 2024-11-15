package com.example.e_chitii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.e_chitii.screens.ChatListScreen
import com.example.e_chitii.screens.LoginScreen
import com.example.e_chitii.screens.ProfileScreen
import com.example.e_chitii.screens.SignUpScreen
import com.example.e_chitii.screens.SingleChatScreen
import com.example.e_chitii.screens.StatusScreen
import com.example.e_chitii.ui.theme.EChitiiTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

sealed class DestinationScreen(route: String){
    val route = route

    object SignUp: DestinationScreen("signup")
    object Login: DestinationScreen("login")
    object Profile: DestinationScreen("profile")

    object ChatList: DestinationScreen("ChatList")
    object SingleChat: DestinationScreen("SingleChat/{userId}"){
        fun createRoute(id: String) = "SingleChat/$id"
    }

    object StatusList: DestinationScreen("StatusList")
    object SingleStatus: DestinationScreen("SingleStatus/{statusId}"){
        fun creteRoute(userId: String) = "SingleStatus/$userId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EChitiiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    ChatAppNavigation()
                }
            }
        }
    }
    @Composable
    fun ChatAppNavigation() {
        val vm: LCViewModel = hiltViewModel()
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route){
            composable(DestinationScreen.SignUp.route){
               SignUpScreen(navController,vm)
          }

            composable(DestinationScreen.Login.route){
                LoginScreen(navController,vm)
            }

            composable(DestinationScreen.ChatList.route) {
                ChatListScreen(navController,vm)
            }

            composable(DestinationScreen.SingleChat.route) {
                val chatID = it.arguments?.getString("userId")
                chatID?.let {
                    SingleChatScreen(navController,vm,chatID)
                }
            }

//            navController.navigate("chat_screen/JohnDoe")
//            composable(
//                route = "chat_screen/{userName}",
//                arguments = listOf(navArgument("userName") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val userName = backStackEntry.arguments?.getString("userName")
//                ChatScreen(userName = userName ?: "Unknown")
//            }
            composable(DestinationScreen.StatusList.route) {
                StatusScreen(navController,vm)
            }

            composable(DestinationScreen.Profile.route) {
                ProfileScreen(navController,vm)
            }
        }


    }
}