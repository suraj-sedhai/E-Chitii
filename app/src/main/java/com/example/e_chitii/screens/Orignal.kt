//package com.example.e_chitii.screens
//
//
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.IntrinsicSize
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.AlertDialogDefaults.containerColor
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldColors
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Devices
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.example.e_chitii.CommonDivider
//import com.example.e_chitii.CommonImage
//import com.example.e_chitii.CommonProgressBar
//import com.example.e_chitii.DestinationScreen
//import com.example.e_chitii.LCViewModel
//import com.example.e_chitii.navigateTo
//
//@Composable
//fun ProfileScreen(navController: NavController, vm: LCViewModel) {
//
//    val inProgress = vm.inProgress.value
//
//    if (inProgress) {
//        CommonProgressBar()
//    } else {
//        val userData = vm.userData.value
//        var name by rememberSaveable {
//            mutableStateOf(userData?.name?: "")
//        }
//        var number by rememberSaveable {
//            mutableStateOf(userData?.number?: "")
//        }
//        Column {
//            Box(
//                modifier = Modifier
//                    .weight(0.9f)
//                    .fillMaxWidth()
//                    .padding(10.dp)
//            ){
//                ProfileContent(modifier = Modifier
//                    .verticalScroll(rememberScrollState())
//                    .padding(8.dp),
//                    vm = vm,
//                    name = name,
//                    number = number,
//                    onNameChange = {name = it},
//                    onNumberChange = {number = it},
//                    onLogOut = {
//                        vm.onLogOut()
//                        navigateTo(navController = navController, route = DestinationScreen.Login.route)
//
//                    },
//                    onBack = { navigateTo(navController = navController, route = DestinationScreen.ChatList.route) },
//                    onSave = {
//                        vm.createOrUpdateProfile(name = name, number = number)
//                    }
//                )
//            }
//            CommonDivider()
//            Box(
//                modifier = Modifier
//                    .weight(0.1f)
//                    .fillMaxWidth()
//                    .padding(bottom = 40.dp)
//            ){BottonNavigationMenu(
//                selectedItem = BottomNavigationItem.PROFILELIST,
//                navController = navController,
//                modifier = Modifier
//
//            )}
//
//
//
//        }
//    }
//}
//
//@Composable
//fun ProfileContent(
//    modifier: Modifier,
//    vm: LCViewModel,
//    name: String ,
//    number: String,
//    onNameChange:(String) -> Unit,
//    onNumberChange:(String) -> Unit,
//    onLogOut: () -> Unit,
//    onBack: () -> Unit = {},
//    onSave: () -> Unit = {}) {
//    val imageUrl = vm.userData.value?.imageUrl
//    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 30.dp, start = 8.dp, end = 8.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
//        ) {
//            Text(text = "Back",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.clickable { onBack() })
//            Text(text = "Save",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.clickable { onSave() })
//        }
//
//        CommonDivider()
//        ProfileImage(imageUrl = imageUrl, vm = vm)
//        CommonDivider()
//
//        Row(modifier = Modifier
//            .padding(vertical = 8.dp)
//            ,verticalAlignment = Alignment.CenterVertically) {
//
//            TextField(value = name
//                ,onValueChange = {onNameChange(it)},
//                colors = TextFieldDefaults.colors().copy(focusedTextColor = Color.Black, focusedContainerColor = Color.Yellow, unfocusedContainerColor = Color.Transparent)
//            )
//        }
//
//        Row(modifier = Modifier
//            .padding(vertical = 8.dp)
//            ,verticalAlignment = Alignment.CenterVertically) {
//
//            TextField(value = number
//                ,onValueChange = onNumberChange,
//                colors = TextFieldDefaults.colors().copy(focusedTextColor = Color.Black, focusedContainerColor = Color.Yellow, unfocusedContainerColor = Color.Transparent)
//            )
//        }
//
//        Row(modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 16.dp), horizontalArrangement = Arrangement.Center) {
//            Button(onClick = { onLogOut()}){
//                Text(text ="Log Out")
//            }
//
//        }
//
//
//
//    }
//}
//
//@Composable
//fun ProfileImage(imageUrl: String? = null, vm: LCViewModel){
//    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
//            uri ->
//        uri?.let {
//            vm.uploadProfileImage(it)
//        }
//    }
//
//    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
//        Column(modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth()
//            .clickable {
//                launcher.launch("image/*")
//            }, horizontalAlignment = Alignment.CenterHorizontally) {
//            Card(
//                onClick = { /*TODO*/ },
//                shape = CircleShape,
//                modifier = Modifier
//                    .padding(8.dp)
//                    .size(100.dp),
//            ) {
//                CommonImage(data = imageUrl, modifier = Modifier)
//
//            }
//            Text(text = "Change profile picture")
//        }
//        if (vm.inProgress.value) {
//            CommonProgressBar()
//        }
//    }
//}
//
