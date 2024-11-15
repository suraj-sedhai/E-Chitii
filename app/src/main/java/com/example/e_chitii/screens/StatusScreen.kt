package com.example.e_chitii.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.e_chitii.CommonProgressBar
import com.example.e_chitii.CommonRow
import com.example.e_chitii.LCViewModel
import com.example.e_chitii.Titletext
import com.example.e_chitii.navigateTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusScreen(navController: NavController, vm: LCViewModel) {
    val inProcess = vm.inProgressStatus.value
    if (inProcess) {
        CommonProgressBar()
    } else {
        val statuses = vm.status.value

        val userData = vm.userData.value

        val myStasuses = statuses.filter {
            it.user?.userId == userData?.userId
        }
        val otherStasuses = statuses.filter {
            it.user?.userId != userData?.userId
        }


        Scaffold(
            floatingActionButton = {
                FAB(
                    onFabClicked = { /*TODO*/ })
            },
            floatingActionButtonPosition = FabPosition.End,
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(90.dp),
                    colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    title = {
                        Text(
                            text = "Stauts",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue
                            )
                        )
                    })
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(90.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    BottonNavigationMenu(
                        selectedItem = BottomNavigationItem.STATUSLIST,
                        navController = navController,
                        modifier = Modifier
                    )
                }


            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                if (statuses.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No Status")
                    }
                }
                else{
                    if(myStasuses.isNotEmpty()){
                        CommonRow(
                            imageUrl = myStasuses[0].user?.imageUrl,
                            name = myStasuses[0].user?.name?:"" ) {
                            navigateTo(navController = navController,
                                route ="" ) //place a route in here
                        }
                    }
                }


            }
        }
    }

}

@Composable
fun FAB(
    onFabClicked: () -> Unit,
) {
    FloatingActionButton(onClick = { onFabClicked() },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        shape = CircleShape,
        modifier = Modifier
    ) {
        Icon(imageVector = Icons.Rounded.Edit, contentDescription = "edit", tint = Color.White)

    }

}