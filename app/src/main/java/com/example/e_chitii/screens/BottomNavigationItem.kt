package com.example.e_chitii.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.e_chitii.DestinationScreen
import com.example.e_chitii.navigateTo
import com.example.e_chitti.R

enum class BottomNavigationItem(val icon: Int, val destinationScreen: DestinationScreen) {
    CHATLIST(icon = R.drawable.chat, destinationScreen = DestinationScreen.ChatList),
    STATUSLIST(
        icon = R.drawable.status, destinationScreen = DestinationScreen.StatusList
    ),
    PROFILELIST(icon = R.drawable.profile, destinationScreen = DestinationScreen.Profile)


}

@Composable
fun BottonNavigationMenu(
    selectedItem: BottomNavigationItem, navController: NavController,modifier: Modifier
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        for (item in BottomNavigationItem.values()) {
            Image(painter = painterResource(id = item.icon),
                contentDescription = "This is icon",
                modifier = Modifier.size(30.dp).clickable {
                    navigateTo(navController,item.destinationScreen.route)
                },
                colorFilter = if (item == selectedItem){
                    ColorFilter.tint(color = Color.Blue)
                }
                else{
                    ColorFilter.tint(color = Color.Gray)
                })
        }
    }
}