package com.example.healthmap.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.example.healthmap.R
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import com.mapbox.maps.extension.style.expressions.dsl.generated.color
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.healthmap.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.collectLatest




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isInPreview = LocalInspectionMode.current
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("310736295843-6vaergprnketq35bg6nf715jkvau83pl.apps.googleusercontent.com") // 🔁 替换成你 Firebase 上的 Web client ID
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        navController.navigate("home")
                    } else {
                        Toast.makeText(context, "Google login failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google sign-in error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    if (!isInPreview && context is Activity) {
        val window = context.window
        SideEffect {
            window.navigationBarColor = Color.Black.toArgb()
            WindowInsetsControllerCompat(window, window.decorView)
                .isAppearanceLightNavigationBars = false
        }
    }

    LaunchedEffect(userViewModel.loginSuccess) {
        userViewModel.loginSuccess.collect { success ->
            if (success) {
                val user = userViewModel.currentUser.value
                val nameToUse = user?.firstName ?: "User"
                navController.navigate("home/$nameToUse")
            } else {
                errorMessage = "Incorrect email or password"
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "HEALTH-MAP",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { padding ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {


            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            // page detail
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "WELCOME!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp)
                )
                Text(
                    text = "T0",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp)
                )
                Text(
                    text = "HEALTH-MAP",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 120.dp)
                )

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )


                Spacer(modifier = Modifier.height(16.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.White) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )

                // Error message
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login button
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Please enter both email and password"
                        } else {
                            errorMessage = null
                            userViewModel.login(email, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RectangleShape
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Sign in with Google")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Forgot Password
                TextButton(onClick = {
                    navController.navigate("reset")
                }) {
                    Text("Forgot Password?", color = Color.White)
                }

                // Register
                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Don't have an account? Register", color = Color.White)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    val genderOptions = listOf("Male", "Female", "Other")
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isInPreview = LocalInspectionMode.current

    if (!isInPreview && context is Activity) {
        val window = context.window
        SideEffect {
            window.navigationBarColor = Color.Black.toArgb()
            WindowInsetsControllerCompat(window, window.decorView)
                .isAppearanceLightNavigationBars = false
        }
    }

    //val registerResult by userViewModel.registerResult.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.registerResult.collectLatest { result ->
            when (result) {
                "SUCCESS" -> {
                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                    userViewModel.resetRegisterResult()
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
                "EXISTS" -> {
                    errorMessage = "Account already exists"
                    userViewModel.resetRegisterResult()
                }
                "FAIL" -> {
                    errorMessage = "Failed to register new account"
                    userViewModel.resetRegisterResult()
                }
                null -> {
                    // do nothing
                }
            }
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Your Account",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 44.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()) {
                        errorMessage = "All fields are required"
                    } else {
                        errorMessage = null
                        userViewModel.register(
                            email,
                            password,
                            firstName,
                            lastName,
                            gender
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RectangleShape
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Login", color = Color.Black)
            }
        }
    }
}