package com.example.gymtracker.utils

fun String.capitalize() =
    this.replaceFirstChar {
        it.titlecase()
    }
