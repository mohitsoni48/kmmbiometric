package com.mohitsoni.kmmbiometric.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.mohitsoni.kmmbiometric.BiometricUtilAndroidImpl
import com.mohitsoni.kmmbiometric.CipherUtilAndroidImpl
import com.mohitsoni.kmmbiometric.views.MainScreen
import com.mohitsoni.kmmbiometric.views.MyApplicationTheme

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
            MainScreen(bioMetricUtil = bioMetricUtil)
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
