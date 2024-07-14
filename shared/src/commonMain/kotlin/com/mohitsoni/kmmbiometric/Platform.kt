package com.mohitsoni.kmmbiometric

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform