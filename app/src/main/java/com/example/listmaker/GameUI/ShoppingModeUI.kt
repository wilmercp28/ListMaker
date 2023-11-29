package com.example.listmaker.GameUI

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import com.example.listmaker.Data.Item
import com.example.listmaker.Data.ListOfItems
import com.example.listmaker.Data.saveDataSettings
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShoppingMode(
    navController: NavHostController,
    listOfItems: SnapshotStateList<ListOfItems>,
    selectedIndex: MutableIntState,
    dataStore: DataStore<Preferences>,
    shoppingMode: MutableState<Boolean>
) {
    val coroutine = rememberCoroutineScope()

    var checkedItems by remember { mutableStateOf(setOf<Item>()) }

    val sortedItems = remember(listOfItems, checkedItems) {
        val uncheckedItems = listOfItems[selectedIndex.value].items.filter { !checkedItems.contains(it) }
        val checkedItems = listOfItems[selectedIndex.value].items.filter { checkedItems.contains(it) }
        (uncheckedItems + checkedItems.sortedBy { it.item }).toList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Shopping Mode",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                navigationIcon = {
                    Button(onClick = {
                        coroutine.launch {
                            saveDataSettings("SHOPPING-MODE", false, dataStore)
                            shoppingMode.value = false
                            navController.navigate("HOME")
                        }
                    }) {
                        Text(text = "Cancel")
                    }
                },
                actions = {
                    Button(onClick = {
                        checkedItems = emptySet()
                    }) {
                        Text(text = "Reset")
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {

            LazyColumn(
                content = {
                    items(sortedItems, key = {it.id}){ item ->
                        Box(
                            modifier = Modifier
                                .animateItemPlacement()
                        ) {
                            ShoppingModeItem(item, checkedItems.contains(item)) {
                                checkedItems = if (it) {
                                    checkedItems + item
                                } else {
                                    checkedItems - item
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingModeItem(item: Item, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isChecked) Color.Green else Color.Red,
        animationSpec = tween(durationMillis = 1000)
    )

    AnimatedContent(targetState = isChecked) { check ->
        Column(
            modifier = Modifier
                .combinedClickable(
                    onClick = { onCheckedChange(true)},
                    onLongClick = { onCheckedChange(false)}
                )
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(5.dp)
                ) {
                    Icon(
                        if (check) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = "Check",
                        tint = Color.Black
                    )
                }
                Text(
                    text = item.quantity,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = item.unit,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = item.item,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(3f)
                )
            }
        }
    }
}
