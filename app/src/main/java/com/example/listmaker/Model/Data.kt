package com.example.listmaker.Model

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope


data class ListOfItems(
    val name: String,
    var items : List<Item>
)


data class Item(
    var quantity : String,
    val item : String
)