package com.example.listmaker.GameUI

import android.util.Log
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.listmaker.Model.Item
import com.example.listmaker.Model.ListOfItems


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifiesList(
    navController: NavHostController,
    listOfItems: MutableList<ListOfItems>,
    index: Int,
    unitTypes: List<String>
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
                actions = {
                    TextButton(onClick = {
                        listOfItems[index] = ListOfItems(mutableNameOfList, mutableListOfItems)
                        navController.navigate("HOME")
                    }) {
                        Text(text = "Save")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        listOfItems.removeAt(index)
                        Log.d("List", listOfItems.toString())
                        navController.navigate("HOME")
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
                        ListItem(item, unitTypes){
                            quantity, Description, unit ->
                            item.unit = unit
                            item.quantity = quantity
                            item.item = Description
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
                            mutableListOfItems += Item("Units", "", "")
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


@Composable
fun ListItem(
    item: Item, unitTypes: List<String>,
    onChange: (quantity: String, Description: String,unit: String) -> Unit
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
        QuantityTextField(quantity, itemDescription, unitTypes, itemUnit) {
            onChange(quantity.value,itemDescription.value,itemUnit.value)
        }
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
    onChange: () -> Unit
) {
    val textStyle = androidx.compose.ui.text.TextStyle(
        fontSize = 17.sp,
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
                .weight(3f)
                .padding(5.dp),
            value = quantity.value,
            onValueChange = { newValue ->
                onChange()
                val dotCount = newValue.count { it == '.' }
                val size = newValue.length
                if (size <= 5) {
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
                if (size <= 25) {
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



