package com.example.csx4109_542_assignment_3_6612054.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "image_store")

class ImageRepository(private val context: Context) {

    private val IMAGE_LIST_KEY = stringPreferencesKey("image_list")

    suspend fun saveImage(imageUri: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[IMAGE_LIST_KEY]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableList() ?: mutableListOf()

            if (!currentList.contains(imageUri)) { // Avoid duplicates
                currentList.add(imageUri)
                preferences[IMAGE_LIST_KEY] = currentList.joinToString(",")
            }
        }
    }

    suspend fun deleteImage(imageUri: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[IMAGE_LIST_KEY]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableList() ?: mutableListOf()

            if (currentList.contains(imageUri)) {
                currentList.remove(imageUri)
                preferences[IMAGE_LIST_KEY] = currentList.joinToString(",")
            }
        }
    }

    suspend fun loadSavedImages(): List<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[IMAGE_LIST_KEY]?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
            }
            .first()
    }
}
