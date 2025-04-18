package com.example.gymtracker.domain

data class Approach(
    val id: Long,
    val ordinal: Int,
    val weight: Float?,
    val repetitions: Int,
)