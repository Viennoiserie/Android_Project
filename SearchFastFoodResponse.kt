package com.example.obesitron.answerClasses

data class SearchFastFoodResponse(
    val elements: List<Element>,
    val generator: String,
    val osm3s: Osm3s,
    val version: Double
)