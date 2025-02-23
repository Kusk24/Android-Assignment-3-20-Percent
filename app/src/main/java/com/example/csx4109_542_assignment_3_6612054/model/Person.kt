package com.example.csx4109_542_assignment_3_6612054.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Person (
    val name : Name = Name(),
    val picture: Picture = Picture()
)