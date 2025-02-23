package com.example.csx4109_542_assignment_3_6612054.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Picture (
    val large : String = "",
    val medium : String = "",
    val thumbnail : String = ""
)