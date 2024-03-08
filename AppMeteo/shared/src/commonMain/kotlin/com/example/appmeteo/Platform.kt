package com.example.appmeteo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform