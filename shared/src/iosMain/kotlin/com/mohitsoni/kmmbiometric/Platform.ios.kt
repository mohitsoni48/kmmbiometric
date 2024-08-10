package com.mohitsoni.kmmbiometric

import androidx.compose.ui.window.ComposeUIViewController
import com.mohitsoni.kmmbiometric.views.MainScreen
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()