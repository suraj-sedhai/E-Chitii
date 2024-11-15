package com.example.e_chitii

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.e_chitti.R

fun navigateTo(navController: NavController,route: String) {
    navController.navigate(route)
}

@Composable
fun CommonProgressBar(){
    Row(modifier = Modifier
        .fillMaxSize()
        .alpha(0.4f)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .clickable(enabled = false) {},
        verticalAlignment = Alignment.CenterVertically
        ){
            CircularProgressIndicator()
    }
}

@Composable
fun CheckSignedIn(vm: LCViewModel,navController: NavController){
    val alreadySignIn = remember { mutableStateOf(false) }
    val signedIn = vm.signIn.value
    if(signedIn && !alreadySignIn.value){
        alreadySignIn.value = true
        navController.navigate(DestinationScreen.ChatList.route){
            popUpTo(0)}
        }
}

@Composable
fun CommonDivider(){
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 0.dp),
        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 1f),
        thickness = 0.3.dp
    )

}

@Composable
fun CommonImage(
    data: String?,
    modifier: Modifier = Modifier, // Default Modifier to make it optional
    contentScale: androidx.compose.ui.layout.ContentScale = androidx.compose.ui.layout.ContentScale.Crop
) {
    if (data == "null") {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Default Profile Image",
            modifier = modifier
                .size(50.dp) // Adjust the size as per your design
                .clip(CircleShape) // Makes the image circular
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape), // Optional border around the image
            contentScale = ContentScale.Crop
        )
    }
    else{
        val painter = rememberAsyncImagePainter(model = data)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier, // Use the passed modifier here
            contentScale = contentScale
        )
    }




}


@Composable
fun Titletext(txt: String){
    Text(text = txt,
        modifier = Modifier.padding(8.dp),fontWeight = FontWeight.Bold, fontSize = 35.sp)

}
@Composable
fun CommonRow(imageUrl: String?,name: String,onItemClick: () -> Unit){

    Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)
        ,border = BorderStroke(width = 1.dp, color = Color.Red)
        ,elevation = CardDefaults.cardElevation(defaultElevation = 75.dp)
        ,modifier = Modifier
            .height(95.dp)
            .padding(8.dp)
            .shadow(4.dp)
        , onClick = { onItemClick() }) {
        Row(modifier = Modifier
            .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically) {

        CommonImage(data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Red)
        )

            Text(text = name?: "------", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
        }

    }


}