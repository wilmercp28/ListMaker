package com.example.listmaker.Data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


val gson = Gson()



suspend fun saveDataList(
    key: String,
    listOfItems: SnapshotStateList<ListOfItems>,
    dataStore: DataStore<Preferences>
) {
    val jsonString = gson.toJson(listOfItems)
    val preferenceKey = stringPreferencesKey(key)
  dataStore.edit { preferences ->
        preferences[preferenceKey] = jsonString
    }
}

suspend fun loadDataList(
    key: String,
    dataStore: DataStore<Preferences>
): SnapshotStateList<ListOfItems> {
    val preferenceKey = stringPreferencesKey(key)

    return dataStore.data.map { preferences ->
        val jsonString = preferences[preferenceKey]
        val loadedData = gson.fromJson<SnapshotStateList<ListOfItems>>(
            jsonString,
            object : TypeToken<SnapshotStateList<ListOfItems>>() {}.type
        )

        loadedData ?: mutableStateListOf()
    }.first()
}



