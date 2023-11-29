package com.example.listmaker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listmaker.Data.ListOfItems
import com.example.listmaker.Data.loadDataList
import com.example.listmaker.Data.loadDataSettings
import com.example.listmaker.GameUI.HomeUI
import com.example.listmaker.GameUI.ModifiesList
import com.example.listmaker.GameUI.ShoppingMode
import com.example.listmaker.ui.theme.ListMakerTheme
import kotlinx.coroutines.delay

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "List")

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListMakerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var listOfItems = remember { mutableStateListOf<ListOfItems>() }
                    val unitTypes = listOf("Unit", "LBS", "OZ", "KG")
                    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }
                    val shoppingMode = rememberSaveable { mutableStateOf(false) }
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "LOADING-SCREEN") {

                        composable("LOADING-SCREEN") {

                            CircularProgressIndicator(modifier = Modifier.fillMaxSize())

                            LaunchedEffect(Unit) {
                                try {
                                    listOfItems = loadDataList("List", dataStore)
                                    selectedIndex.intValue = loadDataSettings("SELECTED-INDEX", dataStore, 0)
                                    shoppingMode.value = loadDataSettings("SHOPPING-MODE", dataStore, false)
                                    Log.d("Shopping Mode is",shoppingMode.value.toString())
                                } catch (e: Exception) {
                                    Log.e("MainActivity", "Error loading data: ${e.message}")
                                } finally {
                                    delay(1000)
                                    if (shoppingMode.value){
                                        navController.navigate("SHOPPING-MODE")
                                    } else {
                                        navController.navigate("HOME")
                                    }
                                }
                            }
                        }
                        composable("HOME") {
                            HomeUI(
                                navController,
                                listOfItems,
                                selectedIndex,
                                dataStore,
                                shoppingMode
                            )
                        }
                        composable("SHOPPING-MODE") {
                            ShoppingMode(
                                navController,
                                listOfItems,
                                selectedIndex,
                                dataStore,
                                shoppingMode
                            )
                        }
                        composable("NEW-LIST") {
                            ModifiesList(
                                navController,
                                listOfItems,
                                listOfItems.lastIndex,
                                unitTypes,
                                dataStore
                            )
                        }
                        composable("MODIFY-LIST") {
                            ModifiesList(
                                navController,
                                listOfItems,
                                selectedIndex.intValue,
                                unitTypes,
                                dataStore,
                            )
                        }
                    }
                }
            }
        }
    }
}
