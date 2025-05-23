package com.example.healthmap.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthmap.R
import com.example.healthmap.ui.component.AppTopBar
import com.example.healthmap.viewmodel.UserViewModel
import androidx.compose.ui.draw.shadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.currentUser.collectAsState().value
    val fullName = "${user?.firstName ?: "First"} ${user?.lastName ?: "Last"}"
    val gender = "Gender: ${user?.gender ?: "-"}"
    val email = user?.email ?: "example@domain.com"

    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUserFromFirebase()
    }

    // ✅ 背景图不随滚动
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.home_background), // 替换成你自己的背景图资源名
            contentDescription = "Profile Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Profile",
                    isHomeScreen = false,
                    onNavigationClick = { navController.popBackStack() }
                )
            },
            containerColor = Color.Transparent // ✅ 让背景图透出
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                ProfileCard(
                    title = "Personal Information",
                    content = listOf(fullName, gender),
                    navController = navController,
                    editable = true
                )

                ProfileCard(
                    title = "Email Address",
                    content = listOf(email),
                    navController = navController,
                    editable = false
                )
            }
        }
    }
}

@Composable
fun ProfileCard(
    title: String,
    content: List<String>,
    navController: NavController,
    editable: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(160.dp) // ✅ 固定高度让所有卡片一致
            .shadow(4.dp, shape = MaterialTheme.shapes.medium), // ✅ 阴影
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                content.forEach {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (editable) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Edit",
                        modifier = Modifier
                            .clickable { navController.navigate("edit_profile") }
                            .padding(top = 8.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}