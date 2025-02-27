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
import com.example.csx4109_542_assignment_3_6612054.repository.ImageRepository
import com.example.csx4109_542_assignment_3_6612054.repository.PersonEntityRepository
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

    private val personRepository = PersonEntityRepository(application)
    private val imageRepository = ImageRepository(application)

    val personEntityList = personRepository.personEntityList

    private val _personList = MutableStateFlow<List<Person>>(emptyList())
    val personList: StateFlow<List<Person>> = _personList

    private val _imageList = MutableStateFlow<List<String>>(emptyList())
    val imageList: StateFlow<List<String>> = _imageList

    init {
        loadPerson()
        loadSavedImages()
    }

    fun loadPerson() {
        val url = "https://randomuser.me/api"
        viewModelScope.launch {
            val data = httpClient.get(url).body<ResultWrapper>()
            val person = data.results.first()

            _personList.value += person

            savePerson(person)
        }
    }

    fun savePerson(person: Person) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personRepository.insertPerson(person)
            }
        }
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personRepository.deletePerson(person)
            }
        }
    }

    fun deletePersonEntity(personEntity: PersonEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personRepository.deletePersonEntity(personEntity)
            }
        }
    }

    fun saveImage(imageUri: String) {
        viewModelScope.launch {
            imageRepository.saveImage(imageUri)
            loadSavedImages()
        }
    }

    fun deleteImage(imageUri: String) {
        viewModelScope.launch {
            imageRepository.deleteImage(imageUri)
            loadSavedImages()
        }
    }

    fun loadSavedImages() {
        viewModelScope.launch {
            val savedImages = imageRepository.loadSavedImages()
            _imageList.value = savedImages
        }
    }
}
