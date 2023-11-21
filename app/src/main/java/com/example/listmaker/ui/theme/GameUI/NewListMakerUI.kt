package com.example.listmaker.ui.theme.GameUI

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.listmaker.listOfItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeNewListUI(
    navController: NavHostController,
    listOfItems: MutableList<listOfItems>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "New List")
                },
                actions = {
                    TextButton(onClick = {  }) {
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
    ) {
        scaffoldPadding ->
        val numbersOfItems = remember { mutableIntStateOf(0) }
        LazyColumn(
            modifier = Modifier
                .padding(scaffoldPadding)
        ) {
            if (listOfItems.isNotEmpty()) {
                items(listOfItems) {
                    ListItem()
                }
            }
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { numbersOfItems.intValue++ }) {
                        Icon(Icons.Default.Add, contentDescription = "Add New Item")
                    }
                }
            }
        }

    }
}

@Composable
fun ListItem(

){





}
