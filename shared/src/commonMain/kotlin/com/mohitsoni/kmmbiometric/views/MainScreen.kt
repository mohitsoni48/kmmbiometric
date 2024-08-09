package com.mohitsoni.kmmbiometric.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.mohitsoni.kmmbiometric.BioMetricUtil
import com.mohitsoni.kmmbiometric.BiometricAuthorizationViewModel
import com.mohitsoni.kmmbiometric.BiometricEffect
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(modifier: Modifier = Modifier, bioMetricUtil: BioMetricUtil, biometricViewModel: BiometricAuthorizationViewModel) {
    MyApplicationTheme {
        val navHostController = rememberNavController()

        LaunchedEffect(key1 = Unit) {
            biometricViewModel.effect.collectLatest {
                when (it) {
                    BiometricEffect.BiometricAuthSuccess -> {
                        navHostController.navigate("AuthSuccessDialog")
                    }
                    BiometricEffect.BiometricSetSuccess -> {

                        navHostController.navigate("VerifyBiometric")
                    }
                }
            }
        }
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color.White
        ) {
            NavHost(navController = navHostController, startDestination = "SetPublicKey") {
                composable("SetPublicKey") {
                    SetPublicKey(biometricAuthorizationViewModel = biometricViewModel, bioMetricUtil = bioMetricUtil)
                }

                composable("VerifyBiometric") {
                    VerifyBiometric(biometricAuthorizationViewModel = biometricViewModel, bioMetricUtil = bioMetricUtil)
                }

                dialog("AuthSuccessDialog") {
                    Dialog(onDismissRequest = {
                        navHostController.popBackStack()
                    }, content = {
                        Column(
                            modifier = Modifier.background(Color.Gray).padding(all = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Biometric Success")
                            Spacer(Modifier.height(8.dp))
                            Text("OK", modifier = Modifier.padding(all = 8.dp).clickable {
                                navHostController.popBackStack()
                            })
                        }
                    })
                }
            }
        }
    }
}