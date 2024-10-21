package com.example.obesitron.answerClasses

data class SearchRouteResponse(
    val hints: Hints,
    val info: Info,
    val paths: List<Path>
)