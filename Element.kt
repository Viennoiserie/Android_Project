package com.example.obesitron.answerClasses

data class Element(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val tags: Tags,
    val type: String
)