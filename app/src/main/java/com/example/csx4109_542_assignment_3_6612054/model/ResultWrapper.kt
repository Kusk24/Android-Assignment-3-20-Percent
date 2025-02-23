package com.example.csx4109_542_assignment_3_6612054.model

import kotlinx.serialization.Serializable

@Serializable
data class ResultWrapper (
    val results: List<Person> = listOf()
)