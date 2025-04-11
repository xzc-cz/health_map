package com.example.healthmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthmap.ui.MapScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    var showMap by remember { mutableStateOf(false) }

    if (showMap) {
        // ✅ 切换到地图页面
        MapScreen()
    } else {
        // ✅ 显示主页按钮
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                Button(
                    onClick = { showMap = true },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("查看今日计划地图")
                }
            }
        }
    }
}
