package com.example.listmaker.GameUI

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import com.example.listmaker.Data.Item
import com.example.listmaker.Data.ListOfItems
import com.example.listmaker.Data.saveDataList
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ModifiesList(
    navController: NavHostController,
    listOfItems: MutableList<ListOfItems>,
    index: Int,
    unitTypes: List<String>,
    dataStore: DataStore<Preferences>
) {
    val coroutine = rememberCoroutineScope()
    var mutableListOfItems by rememberSaveable {
        mutableStateOf(listOfItems[index].items)
    }
    var mutableNameOfList by rememberSaveable {
        mutableStateOf(listOfItems[index].name)
    }
    var saving by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
                    {
                        val textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = mutableNameOfList,
                            onValueChange = { mutableNameOfList = it },
                            textStyle = textStyle
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        listOfItems[index] = ListOfItems(mutableNameOfList, mutableListOfItems)
                        coroutine.launch {
                            saveDataList(
                                "List",
                                listOfItems as SnapshotStateList<ListOfItems>,
                                dataStore
                            )
                            navController.navigate("HOME")
                        }
                    }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Cancel")
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
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
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
            ) {
                if (mutableListOfItems.isNotEmpty()) {
                    items(mutableListOfItems, key = {it.id}) { item ->
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .height(50.dp)
                        ) {
                            ListItem(item, unitTypes,
                                onChange = { quantity, Description, unit ->
                                    item.unit = unit
                                    item.quantity = quantity
                                    item.item = Description
                                    if (!saving) {
                                        saving = true
                                        coroutine.launch {
                                            saveDataList(
                                                "List",
                                                listOfItems as SnapshotStateList<ListOfItems>,
                                                dataStore
                                            )
                                            saving = false
                                        }
                                    }
                                },
                                onRemove = {
                                    mutableListOfItems = mutableListOfItems - item
                                }
                            )
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {
                            mutableListOfItems = mutableListOfItems + Item("Units", "", "")
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add New Item")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ListItem(
    item: Item, unitTypes: List<String>,
    onChange: (quantity: String, Description: String, unit: String) -> Unit,
    onRemove: () -> Unit
) {
    val quantity = rememberSaveable { mutableStateOf(item.quantity) }
    val itemDescription = rememberSaveable { mutableStateOf(item.item) }
    val itemUnit = rememberSaveable { mutableStateOf(item.unit) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        QuantityTextField(quantity, itemDescription, unitTypes, itemUnit,
            onChange = { onChange(quantity.value, itemDescription.value, itemUnit.value) },
            onRemove = { onRemove() }
        )
        Spacer(modifier = Modifier.weight(1.5f))
        Text(text = item.item)
        Spacer(modifier = Modifier.weight(2f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityTextField(
    quantity: MutableState<String>,
    itemDescription: MutableState<String>,
    unitTypes: List<String>,
    itemUnit: MutableState<String>,
    onChange: () -> Unit,
    onRemove: () -> Unit
) {
    val textStyle = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        UnitDropDownMenu(unitTypes, itemUnit)
        OutlinedTextField(
            modifier = Modifier
                .weight(3f),
            value = quantity.value,
            onValueChange = { newValue ->
                onChange()
                val dotCount = newValue.count { it == '.' }
                val size = newValue.length
                if (size <= 6) {
                    if (dotCount <= 1) {
                        quantity.value = newValue
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            maxLines = 1,
            textStyle = textStyle
        )
        OutlinedTextField(
            modifier = Modifier
                .weight(8f),
            value = itemDescription.value,
            onValueChange = { newValue ->
                onChange()
                val size = newValue.length
                if (size <= 22) {
                    itemDescription.value = newValue
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            maxLines = 1,
            textStyle = textStyle
        )
        IconButton(onClick = { onRemove() }, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.Delete, contentDescription = "remove item")
        }
    }
}

@Composable
fun UnitDropDownMenu(
    unitTypes: List<String>,
    itemUnit: MutableState<String>
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(5.dp)
            .clickable { expanded = true },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = itemUnit.value, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = !expanded }) {
        for (units in unitTypes) {
            DropdownMenuItem(text = { Text(text = units) },
                onClick = {
                    itemUnit.value = units
                    expanded = false
                }
            )
        }
    }
}



