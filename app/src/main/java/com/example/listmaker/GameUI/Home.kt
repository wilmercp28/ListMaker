package com.example.listmaker.GameUI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import com.example.listmaker.Data.ListOfItems
import com.example.listmaker.Data.saveDataList
import com.example.listmaker.Data.saveDataSettings
import com.example.listmaker.Model.addNewList
import com.example.listmaker.test.generateExampleItemsList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeUI(
    navController: NavHostController,
    listOfItems: MutableList<ListOfItems>,
    selectedIndex: MutableState<Int>,
    dataStore: DataStore<Preferences>,
    shoppingMode: MutableState<Boolean>
) {
    val coroutine = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row() {
                        Text(text = "List Maker")
                        Button(onClick = {listOfItems += addNewList(listOfItems,isTest = true) }) {
                            Text(text = "Generate Test Lists")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingButton(
                text = "New List",
                Icons.Default.Add,
                onClick = {
                    listOfItems.add(addNewList(listOfItems))
                    coroutine.launch {
                        saveDataList("List", listOfItems, dataStore)
                        navController.navigate("NEW-LIST")
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        if (listOfItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No List", textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
            ) {


                items(listOfItems) { item ->
                    Box(
                        modifier = Modifier
                            .animateItemPlacement()
                    ) {
                        ItemShow(item, listOfItems, selectedIndex, navController, dataStore,shoppingMode.value)
                    }
                }
            }
        }
    }
}




@Composable
fun ItemShow(
    item: ListOfItems,
    listOfItems: MutableList<ListOfItems>,
    selectedIndex: MutableState<Int>,
    navController: NavHostController,
    dataStore: DataStore<Preferences>,
    shoppingMode: Boolean
) {
    var expand by rememberSaveable { mutableStateOf(false) }
    val numberOfItems = item.items.size
    val coroutine = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(20.dp)
            .clickable { expand = !expand }
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = item.name, fontSize = 30.sp)
        Text(text = "$numberOfItems Items")
        AnimatedVisibility(visible = expand) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { listOfItems.remove(item) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove List")
                }
                IconButton(onClick = {
                    coroutine.launch {
                        selectedIndex.value = listOfItems.indexOf(item)
                        saveDataSettings("SELECTED-INDEX", selectedIndex.value, dataStore)
                        navController.navigate("MODIFY-LIST")
                    }

                }) {
                    Icon(Icons.Default.MailOutline, contentDescription = "Modify List")
                }
                IconButton(onClick = {
                    coroutine.launch {
                        selectedIndex.value = listOfItems.indexOf(item)
                        saveDataSettings("SELECTED-INDEX", selectedIndex.value, dataStore)
                        saveDataSettings("SHOPPING-MODE",true,dataStore)
                        navController.navigate("SHOPPING-MODE")
                    }

                }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping mode")
                }
            }
        }

    }
}

@Composable
fun ExtendedFloatingButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(icon, text) },
        text = { Text(text = text) },
    )
}