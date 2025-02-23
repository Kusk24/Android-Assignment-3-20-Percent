package com.example.csx4109_542_assignment_3_6612054.viewModel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.csx4109_542_assignment_3_6612054.database.PersonEntityDatabase
import com.example.csx4109_542_assignment_3_6612054.httpClient
import com.example.csx4109_542_assignment_3_6612054.model.Person
import com.example.csx4109_542_assignment_3_6612054.model.PersonEntity
import com.example.csx4109_542_assignment_3_6612054.model.ResultWrapper
import androidx.datastore.preferences.preferencesDataStore
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.prefs.Preferences

private val Context.dataStore by preferencesDataStore(name = "image_store")

class PersonViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private val database = Room.databaseBuilder(context,
        PersonEntityDatabase::class.java,
        "personEntity_database").fallbackToDestructiveMigration()
        .build()

    private val personEntityDao = database.PersonEntityDao()

    val personEntityList = personEntityDao.getPersonEntity()

    private val _personList = MutableStateFlow<List<Person>>(emptyList())
    val personList: StateFlow<List<Person>> = _personList

    init{
        loadPerson()
        loadSavedImages()
    }

    fun loadPerson() {
        val url : String = "https://randomuser.me/api"

        viewModelScope.launch {
            val data = httpClient.get(url).body<ResultWrapper>()
            val person = data.results.first()

            _personList.value += person

            insertPersonEntity(person)
        }
    }

    fun savePerson(personEntity: PersonEntity){
        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                personEntityDao.insertPersonEntity(personEntity)
            }
        }
    }

    fun deletePerson(personEntity : PersonEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personEntityDao.deletePersonEntity(personEntity)
            }
        }

    }

    private suspend fun insertPersonEntity(person : Person) {
        val personEntity = PersonEntity(
            title = person.name.title,
            first = person.name.first,
            last = person.name.last,
            large = person.picture.large,
            medium = person.picture.medium,
            thumbnail = person.picture.thumbnail
        )
        withContext(Dispatchers.IO) {
        personEntityDao.insertPersonEntity(personEntity)
        }
    }

    private suspend fun deletePersonEntity(person : Person){
        val personEntity = PersonEntity(
            title = person.name.title,
            first = person.name.first,
            last = person.name.last,
            large = person.picture.large,
            medium = person.picture.medium,
            thumbnail = person.picture.thumbnail
        )
        withContext(Dispatchers.IO) {
            personEntityDao.deletePersonEntity(personEntity)
        }
    }

    companion object {
        val IMAGE_LIST_KEY = stringPreferencesKey("image_list")
    }

    private val _imageList = MutableStateFlow<List<String>>(emptyList())
    val imageList: StateFlow<List<String>> = _imageList

    fun saveImage(imageUri: String) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                val currentList = preferences[IMAGE_LIST_KEY]?.split(",")?.filter { it.isNotBlank() }?.toMutableList() ?: mutableListOf()

                if (!currentList.contains(imageUri)) { // Avoid duplicates
                    currentList.add(imageUri)
                    preferences[IMAGE_LIST_KEY] = currentList.joinToString(",")
                }

                // Update StateFlow to notify UI
                _imageList.value = currentList
            }
        }
    }

    fun deleteImage(imageUri: String) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                val currentList = preferences[IMAGE_LIST_KEY]?.split(",")?.filter { it.isNotBlank() }?.toMutableList() ?: mutableListOf()

                if (currentList.contains(imageUri)) {
                    currentList.remove(imageUri)
                    preferences[IMAGE_LIST_KEY] = currentList.joinToString(",")
                }

                // Update StateFlow to notify UI
                _imageList.value = currentList
            }
        }
    }

    fun loadSavedImages() {
        viewModelScope.launch {
            val savedImages = context.dataStore.data.map { preferences ->
                preferences[IMAGE_LIST_KEY]?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
            }.first()

            _imageList.value = savedImages
        }
}}
