package com.example.listmaker

import androidx.compose.runtime.Composable

data class listOfItems(
    val items : List<item>
)



data class item(
    val quantity : Int,
    val item : String
)