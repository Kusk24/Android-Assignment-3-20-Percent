package com.example.csx4109_542_assignment_3_6612054.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.csx4109_542_assignment_3_6612054.model.PersonEntity

@Dao
interface PersonEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonEntity(personEntity: PersonEntity)

    @Query("SELECT * FROM Personentity ORDER BY id DESC")
    fun getPersonEntity() : LiveData<List<PersonEntity>>

    @Delete()
    suspend fun deletePersonEntity(personEntity: PersonEntity)
}