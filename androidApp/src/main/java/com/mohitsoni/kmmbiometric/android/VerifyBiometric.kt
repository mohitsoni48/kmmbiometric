package com.mohitsoni.kmmbiometric.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohitsoni.kmmbiometric.BioMetricUtil
import com.mohitsoni.kmmbiometric.BiometricAuthorizationViewModel

@Composable
fun VerifyBiometric(
    modifier: Modifier = Modifier,
    biometricAuthorizationViewModel: BiometricAuthorizationViewModel,
    bioMetricUtil: BioMetricUtil
) {
    val biometricState by biometricAuthorizationViewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.biometric),
            contentDescription = "biometric"
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            biometricAuthorizationViewModel.authorizeBiometric(bioMetricUtil)
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Verify Biometric",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(all = 12.dp)
            )
        }
        biometricState.error?.let {
            Text(text = it, fontSize = 14.sp, color = Color.Red, modifier = Modifier.padding(all = 12.dp))
        }
        Spacer(modifier = Modifier.padding(bottom = 20.dp))
    }
}