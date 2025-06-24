package com.example.healthmap.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.example.healthmap.ui.theme.HealthMapTheme

@Composable
private fun DrawerMenuItem(
    text: String,
    onClick: () -> Unit,
    bold: Boolean = true
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = HealthMapTheme.dimensions.spacingSmall)
    )
}

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
            .background(MaterialTheme.colorScheme.surface)
            .padding(HealthMapTheme.dimensions.spacingXL)
    ) {
        Spacer(modifier = Modifier.height(HealthMapTheme.dimensions.spacingXL))

        // 顶部用户信息
        Text(
            text = "${user?.firstName ?: "User"} ${user?.lastName ?: ""}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = user?.email ?: "example@email.com",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(HealthMapTheme.dimensions.spacingXXL + HealthMapTheme.dimensions.spacingLarge))

        // 菜单项
        DrawerMenuItem("Profile") { navController.navigate("Profile") }
        DrawerMenuItem("About App") { navController.navigate("about") }

        Spacer(modifier = Modifier.weight(1f))

        // 登出项
        Text(
            text = "Log Out",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
            modifier = Modifier.clickable {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
    }
}