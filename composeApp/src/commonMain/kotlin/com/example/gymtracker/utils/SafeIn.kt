package com.example.gymtracker.utils

infix fun <T> T?.safeIn(collection: Collection<T>?): Boolean {
    return collection?.contains(this) == true
}
