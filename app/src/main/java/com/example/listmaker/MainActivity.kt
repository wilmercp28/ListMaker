package com.example.listmaker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listmaker.Data.ListOfItems
import com.example.listmaker.Data.loadDataList
import com.example.listmaker.GameUI.HomeUI
import com.example.listmaker.GameUI.ModifiesList
import com.example.listmaker.ui.theme.ListMakerTheme

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
                    var loading by rememberSaveable { mutableStateOf(false) }
                    val navController = rememberNavController()
                    var listOfItems = remember { mutableStateListOf<ListOfItems>() }


                    val unitTypes = listOf("Unit", "LBS", "OZ", "KG")
                    val selectedIndex = rememberSaveable { mutableStateOf(0) }
                    NavHost(navController = navController, startDestination = "HOME") {

                        composable("HOME") {
                            LaunchedEffect(Unit){
                                loading = true
                                listOfItems = loadDataList("List",dataStore)
                                loading = false
                            }
                            if (!loading) {
                                HomeUI(navController, listOfItems, selectedIndex, dataStore)
                            }
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
                                selectedIndex.value,
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
