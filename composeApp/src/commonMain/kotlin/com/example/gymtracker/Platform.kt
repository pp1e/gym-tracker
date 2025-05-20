package com.example.gymtracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
