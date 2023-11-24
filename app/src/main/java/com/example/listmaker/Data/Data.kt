package com.example.listmaker.Data

import java.util.UUID


data class ListOfItems(
    val name: String,
    var items : List<Item>
)


data class Item(
    var unit: String,
    var quantity : String,
    var item : String,
    val id: String = UUID.randomUUID().toString(),
)