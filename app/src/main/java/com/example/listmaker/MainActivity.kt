package com.example.listmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listmaker.Model.ListOfItems
import com.example.listmaker.GameUI.HomeUI
import com.example.listmaker.GameUI.ModifiesList
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
                    val listOfItems = remember { mutableStateListOf<ListOfItems>() }

                    NavHost(navController = navController, startDestination = "HOME") {

                        composable("HOME") {
                            HomeUI(navController)
                        }

                        composable("NEW-LIST"){
                            listOfItems.add(ListOfItems("New List", emptyList()))
                            ModifiesList(navController,listOfItems,listOfItems.lastIndex)
                        }
                    }
                }
            }
        }
    }
}
