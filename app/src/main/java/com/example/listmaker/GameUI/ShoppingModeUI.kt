package com.example.listmaker.GameUI

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import com.example.listmaker.Data.Item
import com.example.listmaker.Data.ListOfItems

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShoppingMode(
    navController: NavHostController,
    listOfItems: SnapshotStateList<ListOfItems>,
    selectedIndex: MutableIntState,
    dataStore: DataStore<Preferences>,
    shoppingMode: MutableState<Boolean>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Shopping Mode", textAlign = TextAlign.Center,
                    )
                },
                navigationIcon = {
                    Text(text = "Cancel", modifier = Modifier.clickable {
                        shoppingMode.value = false
                        navController.navigate("HOME")
                    }
                    )
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
                    items(listOfItems[selectedIndex.intValue].items, key = { it.id }) { item ->
                        Box(
                            modifier = Modifier
                                .animateItemPlacement()
                        ) {
                            ShippingModeItem(item)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ShippingModeItem(item: Item) {
    var checkAnimation by rememberSaveable(item.checked) { mutableStateOf(item.checked) }
    AnimatedContent(targetState = checkAnimation) { check ->
        Column(
            modifier = Modifier
                .clickable { checkAnimation = !checkAnimation }
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (check) Color.Green else Color.Red, RoundedCornerShape(10.dp))
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
                Text(text = item.quantity, fontSize = 20.sp, color = Color.Black, modifier = Modifier.weight(1f))
                Text(text = item.unit, fontSize = 20.sp, color = Color.Black, modifier = Modifier.weight(2f))
                Text(text = item.item, fontSize = 20.sp, color = Color.Black, modifier = Modifier.weight(3f))
            }
        }
    }
}