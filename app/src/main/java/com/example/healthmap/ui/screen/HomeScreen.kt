package com.example.healthmap.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.healthmap.R
import com.example.healthmap.ui.component.AppDrawer
import com.example.healthmap.ui.component.AppTopBar
import com.example.healthmap.viewmodel.PlanViewModel
import com.example.healthmap.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    planViewModel: PlanViewModel = viewModel()
) {
    planViewModel.updateAllFromCloud()
    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.currentUser.collectAsState()
    val userName = user?.firstName ?: "User"
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val plans by planViewModel.allPlans.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUserFromFirebase()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(navController = navController)
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Health-Map",
                    isHomeScreen = true,
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = Color(0xFFF2F2F2)
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hi!  $userName",
                        modifier = Modifier.padding(horizontal = 10.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.home1),
                        contentDescription = "Home Image",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .height(600.dp)
                    )
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .height(50.dp)
                                .background(Color.Black)
                                .clickable {
                                    navController.navigate("form/$userName")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Create New Schedule",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Text(
                        text = "Activities",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                items(plans) { plan ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .shadow(4.dp, RoundedCornerShape(12.dp))
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {

                            Text(
                                text = plan.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Text(
                                text = plan.activity,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = "${plan.date}  ${plan.time}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        navController.navigate("map/${plan.lat}/${plan.lng}")
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("View Location")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}