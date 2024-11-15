package com.example.e_chitii.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.e_chitii.CheckSignedIn
import com.example.e_chitii.CommonProgressBar
import com.example.e_chitii.DestinationScreen
import com.example.e_chitii.LCViewModel
import com.example.e_chitii.navigateTo
import com.example.e_chitti.R

@Composable
fun LoginScreen(navController: NavController
                 ,vm: LCViewModel
) {

    CheckSignedIn(vm = vm, navController = navController)
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 38.dp)
        .background(MaterialTheme.colorScheme.surfaceContainer)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .verticalScroll(
                rememberScrollState()
            )
            .background(
                (MaterialTheme.colorScheme.surfaceContainer)
            )
            , horizontalAlignment = Alignment.CenterHorizontally) {


            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }

            val focus = LocalFocusManager.current


            Image(
                painter =
                painterResource(id = R.drawable.messenger)
                ,contentDescription ="Messaging"
                ,modifier = Modifier
                    .padding(8.dp)
                    .height(100.dp)
                    .width(100.dp)
            )

            Text(
                text = "SignUp",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )


            OutlinedTextField(
                value = emailState.value,
                onValueChange = {
                    emailState.value = it
                },
                modifier = Modifier.padding(8.dp),
                label = {
                    Text(text = "email")
                }
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {
                    passwordState.value = it
                },
                modifier = Modifier.padding(8.dp),
                label = {
                    Text(text = "Password")
                }
            )

            Button(onClick = {vm.logIn(emailState.value.text,passwordState.value.text)}){
                Text(text = "Login")
            }

            Text(text = "New user?  Go to SignUp ->",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navigateTo(navController, DestinationScreen.SignUp.route) })

        }

    }

    if (vm.inProgress.value){
        CommonProgressBar()

    }
}