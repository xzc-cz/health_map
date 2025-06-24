package com.example.healthmap.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.healthmap.R
import com.example.healthmap.ui.component.AppDrawer
import com.example.healthmap.ui.component.AppTopBar
import com.example.healthmap.ui.component.ActivityCard
import com.example.healthmap.ui.component.PrimaryButton
import com.example.healthmap.ui.component.SectionHeader
import com.example.healthmap.ui.component.WelcomeText
import com.example.healthmap.ui.component.HealthMapDivider
import com.example.healthmap.ui.theme.HealthMapTheme
import com.example.healthmap.viewmodel.PlanViewModel
import com.example.healthmap.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

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
                containerColor = Color.Transparent
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(horizontal = HealthMapTheme.dimensions.spacingMedium),
                    verticalArrangement = Arrangement.spacedBy(HealthMapTheme.dimensions.spacingMedium)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(HealthMapTheme.dimensions.spacingMedium))
                        WelcomeText(
                            greeting = "Hi! $userName",
                            modifier = Modifier.padding(horizontal = HealthMapTheme.dimensions.spacingSmall)
                        )
                        Spacer(modifier = Modifier.height(HealthMapTheme.dimensions.spacingLarge))
                    }

                    item {
                        Image(
                            painter = painterResource(id = R.drawable.home1),
                            contentDescription = "Home Image",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = HealthMapTheme.dimensions.spacingSmall)
                                .height(480.dp)
                        )
                    }

                    item {
                        PrimaryButton(
                            text = "Create New Schedule",
                            onClick = { navController.navigate("form/$userName") },
                            icon = Icons.Default.Add,
                            modifier = Modifier.padding(horizontal = HealthMapTheme.dimensions.spacingSmall)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(HealthMapTheme.dimensions.spacingMedium))
                        HealthMapDivider(
                            modifier = Modifier.padding(horizontal = HealthMapTheme.dimensions.spacingSmall)
                        )
                        Spacer(modifier = Modifier.height(HealthMapTheme.dimensions.spacingMedium))
                    }

                    item {
                        SectionHeader(
                            title = "Activities",
                            subtitle = "Your upcoming health activities",
                            modifier = Modifier.padding(horizontal = HealthMapTheme.dimensions.spacingSmall)
                        )
                    }

                    items(plans) { plan ->
                        ActivityCard(
                            title = plan.name,
                            activity = plan.activity,
                            datetime = "${plan.date}  ${plan.time}",
                            onMapClick = {
                                navController.navigate("map/${plan.lat}/${plan.lng}")
                            },
                            modifier = Modifier.padding(horizontal = HealthMapTheme.dimensions.spacingSmall)
                        )
                    }
                }
            }
        }
    }
}