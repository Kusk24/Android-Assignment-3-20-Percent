package com.example.csx4109_542_assignment_3_6612054.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.csx4109_542_assignment_3_6612054.dao.PersonEntityDao
import com.example.csx4109_542_assignment_3_6612054.model.PersonEntity

@Database(entities = [PersonEntity::class] , version = 1)
abstract class PersonEntityDatabase: RoomDatabase() {
    abstract fun PersonEntityDao() : PersonEntityDao
}