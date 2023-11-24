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
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeUI(
    navController: NavHostController,
    listOfItems: SnapshotStateList<ListOfItems>,
    selectedIndex: MutableState<Int>,
    dataStore: DataStore<Preferences>
) {
    val coroutine = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "List Maker")
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingButton(
                "New List",
                Icons.Default.Add,
                onClick = {
                    listOfItems.add(ListOfItems("New List", emptyList()))
                    coroutine.launch {
                        saveDataList("List",listOfItems,dataStore)
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
                items(listOfItems, key = { it.name }) { item ->
                    Box(
                        modifier = Modifier
                            .animateItemPlacement()
                    ) {
                        ItemShow(item, listOfItems,selectedIndex,navController)
                    }
                }
            }
        }
    }
}


@Composable
fun ItemShow(
    item: ListOfItems,
    listOfItems: SnapshotStateList<ListOfItems>,
    selectedIndex: MutableState<Int>,
    navController: NavHostController
) {
    var expand by rememberSaveable { mutableStateOf(false) }
    val numberOfItems = item.items.size

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
                    selectedIndex.value = listOfItems.indexOf(item)
                    navController.navigate("MODIFY-LIST")

                }) {
                    Icon(Icons.Default.MailOutline, contentDescription = "Modify List")
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