package com.snoutscout.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snoutscout.app.R
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.SSButton
import com.snoutscout.app.core.ui.SSInput

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    var isRegister by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showOtp by remember { mutableStateOf(false) }
    var otp by remember { mutableStateOf(listOf("", "", "", "")) }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Box(
            modifier = Modifier.size(72.dp).clip(CircleShape).background(SnoutScoutColors.Primary.copy(0.12f)),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Snout Scout Logo",
                modifier = Modifier.size(72.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            if (showOtp) "Enter OTP" else if (isRegister) "Create account" else "Welcome back",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (showOtp) "Enter the 4-digit code sent to +91 $phone" else "Sign in to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))

        if (showOtp) {
            OtpInput(otp = otp, onOtpChange = { otp = it })
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = { showOtp = false }) { Text("Change number") }
            Spacer(Modifier.height(24.dp))
            SSButton(
                text = "Verify OTP",
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
                enabled = otp.all { it.isNotEmpty() }
            )
        } else {
            if (isRegister) {
                SSInput(value = name, onValueChange = { name = it }, label = "Full Name", placeholder = "Aarav Sharma")
                Spacer(Modifier.height(16.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                ) { Text("+91", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium) }
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = phone, onValueChange = { if (it.length <= 10) phone = it },
                    label = { Text("Phone number") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
            SSButton(
                text = if (isRegister) "Create Account" else "Send OTP",
                onClick = { showOtp = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = phone.length == 10 && (!isRegister || name.isNotBlank())
            )
        }

        Spacer(Modifier.weight(1f))
        if (!showOtp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (isRegister) "Already have an account?" else "New here?", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = { isRegister = !isRegister }) {
                    Text(if (isRegister) "Sign In" else "Create Account")
                }
            }
        }
    }
}

@Composable
private fun OtpInput(otp: List<String>, onOtpChange: (List<String>) -> Unit) {
    val focusRequesters = remember { List(4) { FocusRequester() } }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        otp.forEachIndexed { i, digit ->
            OutlinedTextField(
                value = digit,
                onValueChange = { v ->
                    if (v.length <= 1 && v.all { it.isDigit() }) {
                        val newOtp = otp.toMutableList().also { it[i] = v }
                        onOtpChange(newOtp)
                        if (v.isNotEmpty() && i < 3) focusRequesters[i + 1].requestFocus()
                    }
                },
                modifier = Modifier.width(60.dp).height(64.dp).focusRequester(focusRequesters[i]),
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (digit.isNotEmpty()) SnoutScoutColors.Primary else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (digit.isNotEmpty()) SnoutScoutColors.Primary else MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}
