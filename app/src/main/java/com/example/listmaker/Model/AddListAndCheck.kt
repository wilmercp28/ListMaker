package com.example.listmaker.Model

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.listmaker.Data.ListOfItems
import java.time.LocalDate

fun addNewList(listOfItems: MutableList<ListOfItems>): ListOfItems {
    var count = 1
    var listName = "New List $count"
    while (nameExistFromString(listOfItems,listName)) {
        count++
        listName = "New List $count"
        Log.d("New List Name", listName)
    }
    return ListOfItems(name = listName, items = emptyList(),LocalDate.now() )
}

fun nameExistFromObject(listOfItems: SnapshotStateList<ListOfItems>, list: ListOfItems,listName: String): Boolean {
    val filterList = listOfItems.filter { it != list }
    Log.d("Filter list",filterList.toString())
    return  filterList.any { it.name.equals(listName, ignoreCase = true)  }
}

fun nameExistFromString(listOfItems: MutableList<ListOfItems>, list: String): Boolean {
    return listOfItems.any { it.name.equals(list, ignoreCase = true) }
}