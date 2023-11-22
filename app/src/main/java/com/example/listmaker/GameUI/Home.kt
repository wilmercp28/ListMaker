package com.example.listmaker.GameUI

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUI(
    navController: NavHostController
) {
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
                    navController.navigate("NEW-LIST")
                }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
        ) {

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