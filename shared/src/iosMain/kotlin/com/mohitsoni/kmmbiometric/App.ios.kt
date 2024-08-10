package com.mohitsoni.kmmbiometric

import androidx.compose.ui.window.ComposeUIViewController
import com.mohitsoni.kmmbiometric.views.MainScreen
import platform.UIKit.UIViewController

fun MainViewController(
    bioMetricUtil: BioMetricUtil,
    biometricViewModel: BiometricAuthorizationViewModel
): UIViewController = ComposeUIViewController {
    MainScreen(bioMetricUtil = bioMetricUtil, biometricViewModel = biometricViewModel)
}