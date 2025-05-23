package com.example.healthmap.ui.screen

import com.example.healthmap.ui.component.AppTopBar
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.healthmap.ui.theme.HealthMapTheme
import com.example.healthmap.viewmodel.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.example.healthmap.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val context = LocalContext.current
    var email by remember { mutableStateOf(TextFieldValue("")) } // Input field for user's email
    var message by remember { mutableStateOf<String?>(null) } // Message to display feedback
    var isLoading by remember { mutableStateOf(false) } // Tracks whether loading is in progress

    // Collect reset result from ViewModel to show success/failure and navigate accordingly
    LaunchedEffect(Unit) {
        userViewModel.resetResult.collect { success ->
            isLoading = false
            if (success) {
                Toast.makeText(context, "Reset email sent! Check your inbox.", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo("reset") { inclusive = true }
                }
            } else {
                message = "Account does not exist or sending failed."
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image for aesthetics
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = "Reset Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Main scaffold layout with transparent background
        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Reset Password",
                    isHomeScreen = false,
                    onNavigationClick = { navController.popBackStack() }
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Instruction text
                Text(
                    text = "Enter your email to receive a password reset link",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Email input field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Loading state with cancel button
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            isLoading = false
                            message = "Cancelled by user"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }
                } else {
                    // Reset button (enabled when not loading)
                    Button(
                        onClick = {
                            if (email.text.isBlank()) {
                                message = "Email cannot be empty"
                            } else {
                                message = null
                                isLoading = true
                                userViewModel.resetPassword(email.text)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Reset Password",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Message feedback text (e.g., errors or cancel messages)
                message?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(it, color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}





