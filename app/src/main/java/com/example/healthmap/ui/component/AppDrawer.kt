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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.healthmap.viewmodel.UserViewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun AppDrawer(navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.currentUser.collectAsState().value

    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUserFromFirebase()
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Black)
            .padding(36.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "${user?.firstName ?: "User"} ${user?.lastName ?: ""}",
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = user?.email ?: "example@email.com",
            fontSize = 12.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            "Profile",
            color = Color.White,
            modifier = Modifier.clickable { navController.navigate("Profile") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "About App",
            color = Color.White,
            modifier = Modifier.clickable {
                navController.navigate("about")
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Log Out",
            color = Color.White,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.clickable {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
        )
    }
}