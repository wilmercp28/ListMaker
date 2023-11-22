package com.example.listmaker.GameUI

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.listmaker.Model.Item
import com.example.listmaker.Model.ListOfItems


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifiesList(
    navController: NavHostController,
    listOfItems: SnapshotStateList<ListOfItems>,
    index: Int
) {
    var mutableListOfItems by rememberSaveable {
        mutableStateOf(listOfItems[index].items)
    }
    var mutableNameOfList by rememberSaveable {
        mutableStateOf(listOfItems[index].name)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "New List")
                },
                actions = {
                    TextButton(onClick = { }) {
                        Text(text = "Save")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("HOME") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.5f))
                Text(text = "Quantity")
                Spacer(modifier = Modifier.weight(1.5f))
                Text(text = "Item")
                Spacer(modifier = Modifier.weight(2f))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                if (mutableListOfItems.isNotEmpty()) {
                    items(mutableListOfItems) { item ->
                        ListItem(item)
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {
                            mutableListOfItems += Item("", "")
                            Log.d("List Of Items", listOfItems.toString())
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add New Item")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(item: Item) {
    val quantity = rememberSaveable(item.quantity) {
        mutableStateOf(
            item.quantity.toString()
        )
    }
    val itemDescription = rememberSaveable { mutableStateOf(item.item) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        QuantityTextField(quantity,itemDescription)
        Spacer(modifier = Modifier.weight(1.5f))
        Text(text = item.item)
        Spacer(modifier = Modifier.weight(2f))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuantityTextField(quantity: MutableState<String>, itemDescription: MutableState<String>) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        OutlinedTextField(
            modifier = Modifier
                .weight(3f)
                .padding(10.dp),
            value = quantity.value,
            onValueChange = { newValue ->
                val dotCount = newValue.count { it == '.' }
                if (dotCount <= 1) {
                    quantity.value = newValue
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            maxLines = 1
        )
        OutlinedTextField(
            modifier = Modifier
                .weight(8f)
                .padding(10.dp),
            value = itemDescription.value,
            onValueChange = { newValue ->
                    itemDescription.value = newValue
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            maxLines = 1
        )
    }
}



