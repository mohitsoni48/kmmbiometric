package com.mohitsoni.kmmbiometric.android

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohitsoni.kmmbiometric.BiometricAuthorizationViewModel
import com.mohitsoni.kmmbiometric.BiometricEffect
import com.mohitsoni.kmmbiometric.BiometricUtilAndroidImpl
import com.mohitsoni.kmmbiometric.CipherUtilAndroidImpl
import kotlinx.coroutines.flow.collectLatest

class MainActivity : FragmentActivity() {
    private val bioMetricUtil by lazy {
        BiometricUtilAndroidImpl(this, CipherUtilAndroidImpl())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bioMetricUtil.preparePrompt(
            "Biometric",
            "Sample Biometric",
            "This biometric is used for 2FA"
        )
        setContent {
            MyApplicationTheme {
                val navHostController = rememberNavController()
                val biometricViewModel: BiometricAuthorizationViewModel = viewModel()

                LaunchedEffect(key1 = Unit) {
                    biometricViewModel.effect.collectLatest {
                        when (it) {
                            BiometricEffect.BiometricAuthSuccess -> {
                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle("Biometric Success")
                                    .setNegativeButton("OK") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                            }
                            BiometricEffect.BiometricSetSuccess -> {
                                Toast.makeText(this@MainActivity, "Biometric Set Success", Toast.LENGTH_SHORT).show()
                                navHostController.navigate("VerifyBiometric")
                            }
                        }
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navHostController, startDestination = "SetPublicKey") {
                        composable("SetPublicKey") {
                            SetPublicKey(biometricAuthorizationViewModel = biometricViewModel, bioMetricUtil = bioMetricUtil)
                        }

                        composable("VerifyBiometric") {
                            VerifyBiometric(biometricAuthorizationViewModel = biometricViewModel, bioMetricUtil = bioMetricUtil)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
