package com.example.listmaker.test

import com.example.listmaker.Data.Item
import com.example.listmaker.Data.ListOfItems
import java.time.LocalDate

fun generateExampleItemsList(): List<Item> {
    val exampleItems = listOf(
        Item(unit = "KG", quantity = "2", item = "Apples"),
        Item(unit = "Pieces", quantity = "5", item = "Bread"),
        Item(unit = "Liters", quantity = "1", item = "Milk"),
        Item(unit = "Box", quantity = "3", item = "Cereal"),
        Item(unit = "LBS", quantity = "1", item = "Chicken"),
        Item(unit = "OZ", quantity = "8", item = "Cheese"),
        Item(unit = "Grams", quantity = "500", item = "Pasta"),
        Item(unit = "Gallons", quantity = "1", item = "Orange Juice"),
        Item(unit = "Pounds", quantity = "2", item = "Ground Beef"),
        Item(unit = "Dozen", quantity = "1", item = "Eggs"),
        Item(unit = "Quart", quantity = "2", item = "Yogurt"),
        Item(unit = "Pint", quantity = "1", item = "Ice Cream"),
        Item(unit = "Cup", quantity = "2", item = "Flour"),
        // Additional items
        Item(unit = "KG", quantity = "1", item = "Bananas"),
        Item(unit = "Pieces", quantity = "10", item = "Tomatoes"),
        Item(unit = "Liters", quantity = "2", item = "Orange Soda"),
        Item(unit = "Box", quantity = "2", item = "Chocolate"),
        Item(unit = "LBS", quantity = "0.5", item = "Salmon"),
        Item(unit = "OZ", quantity = "16", item = "Peanut Butter"),
        Item(unit = "Grams", quantity = "250", item = "Rice"),
        Item(unit = "Gallons", quantity = "1", item = "Milk (Almond)"),
        Item(unit = "Pounds", quantity = "1", item = "Ham"),
        Item(unit = "Dozen", quantity = "1", item = "Bagels"),
        Item(unit = "Quart", quantity = "1", item = "Tomato Sauce"),
        Item(unit = "Pint", quantity = "1", item = "Strawberries"),
        Item(unit = "Cup", quantity = "1", item = "Sugar")
    )

    return exampleItems
}