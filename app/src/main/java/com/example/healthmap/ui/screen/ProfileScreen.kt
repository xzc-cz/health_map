package com.example.healthmap.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthmap.ui.component.AppTopBar
import com.example.healthmap.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen( navController: NavController) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.currentUser.collectAsState().value

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
                content = listOf(
                    "${user?.firstName ?: "First"} ${user?.lastName ?: "Last"}",
                    "Gender: ${user?.gender ?: "-"}"
                )
            )

            ProfileCard(
                title = "Email Address",
                content = listOf(user?.email ?: "example@domain.com")
            )
            ProfileCard(title = "Password", content = listOf("**********"))
        }
    }
}

@Composable
fun ProfileCard(title: String, content: List<String>) {
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
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}