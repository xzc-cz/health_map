package com.example.healthmap.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthmap.ui.component.AppTopBar
import com.example.healthmap.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen( navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.currentUser.collectAsState().value
    val fullName = "${user?.firstName ?: "First"} ${user?.lastName ?: "Last"}"
    val gender = "Gender: ${user?.gender ?: "-"}"
    val email = user?.email ?: "example@domain.com"

    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUserFromFirebase()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Profile",
                isHomeScreen = false,
                onNavigationClick = {navController.popBackStack()}
            )
        }
    ){ innerPadding ->
        Column(modifier = Modifier.padding(16.dp).padding(innerPadding)) {
            ProfileCard(
                title = "Personal Information",
                content = listOf(fullName, gender),
                navController = navController
            )

            ProfileCard(
                title = "Email Address",
                content = listOf(email),
                navController = navController
            )
        }
    }
}

@Composable
fun ProfileCard(title: String, content: List<String>, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(160.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                content.forEach { Text(it) }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Edit",
                    modifier = Modifier
                        .clickable {
                            navController.navigate("edit_profile")
                        }
                        .padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}