package com.example.healthmap.ui.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AppDrawer(navController: NavController){
    Column (modifier = Modifier.fillMaxHeight().background(Color.Black).padding(36.dp)){
        Spacer(modifier = Modifier.height(32.dp))
        Text("User Name", fontWeight = FontWeight.Bold, color = Color.White)
        Text("xxxxxxx@xxxxxx.com", fontSize = 12.sp, color = Color.White)
        Spacer(modifier = Modifier.height(48.dp))
        Text("Profile", color = Color.White, modifier = Modifier.clickable { navController.navigate("Profile") })
        Spacer(modifier = Modifier.height(8.dp))
        Text("About App", color = Color.White, modifier = Modifier.clickable {  })
        Spacer(modifier = Modifier.weight(1f))
        Text("Log Out", modifier = Modifier.clickable {  }, color = Color.White)
    }
}