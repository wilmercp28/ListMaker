package com.example.listmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listmaker.ui.theme.GameUI.HomeUI
import com.example.listmaker.ui.theme.GameUI.MakeNewListUI
import com.example.listmaker.ui.theme.ListMakerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListMakerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "HOME") {
                        composable("HOME") {
                            HomeUI(navController)
                        }
                        composable("NEW-LIST"){
                            MakeNewListUI(navController)
                        }
                    }
                }
            }
        }
    }
}
