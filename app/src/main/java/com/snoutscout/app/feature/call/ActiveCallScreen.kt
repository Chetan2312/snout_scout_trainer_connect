package com.snoutscout.app.feature.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.util.DateFormatter
import com.snoutscout.app.data.model.CallState
import com.snoutscout.app.data.model.CallType
import com.snoutscout.app.data.model.MockData

@Composable
fun ActiveCallScreen(
    trainerId: String,
    dogId: String,
    callType: String,
    walletBalance: Int,
    onCallEnd: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val trainer = remember { MockData.TRAINERS.find { it.id == trainerId } }
    val ratePerMin = trainer?.ratePerMin ?: 12
    val viewModel: CallViewModel = viewModel(
        factory = CallViewModelFactory(container, walletBalance, ratePerMin)
    )
    val callState by viewModel.callState.collectAsStateWithLifecycle()
    val elapsed by viewModel.elapsed.collectAsStateWithLifecycle()
    val costSoFar by viewModel.costSoFar.collectAsStateWithLifecycle()
    val isMuted by viewModel.isMuted.collectAsStateWithLifecycle()
    val isSpeakerOn by viewModel.isSpeakerOn.collectAsStateWithLifecycle()
    val isVideoOn by viewModel.isVideoOn.collectAsStateWithLifecycle()
    val showLowBalance by viewModel.showLowBalanceWarning.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initiateCall(trainerId, if (callType == "VIDEO") CallType.VIDEO else CallType.VOICE)
    }

    LaunchedEffect(callState) {
        if (callState == CallState.ENDED) {
            onCallEnd(elapsed, costSoFar)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1816)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(60.dp))
            if (callState == CallState.CONNECTING) {
                Text("Connecting...", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(24.dp))
                CircularProgressIndicator(color = SnoutScoutColors.PrimaryLight)
            } else {
                Box(
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(SnoutScoutColors.Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        trainer?.name?.split(" ")?.take(2)?.joinToString("") { it.first().toString() } ?: "?",
                        color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(trainer?.name ?: "", color = Color.White, style = MaterialTheme.typography.titleLarge)
                Text(if (callType == "VIDEO") "Video Call" else "Voice Call",
                    color = Color.White.copy(0.6f), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(32.dp))
                Text(DateFormatter.formatElapsed(elapsed), color = Color.White,
                    fontSize = 42.sp, fontWeight = FontWeight.W300)
                Spacer(Modifier.height(8.dp))
                Text("Cost: ₹$costSoFar", color = SnoutScoutColors.Accent, style = MaterialTheme.typography.titleMedium)
                val remaining = walletBalance - costSoFar
                Text("Balance: ₹$remaining", color = Color.White.copy(0.6f), style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { (costSoFar.toFloat() / walletBalance.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier.width(200.dp).clip(RoundedCornerShape(999.dp)),
                    color = if (remaining < 100) SnoutScoutColors.Error else SnoutScoutColors.Primary
                )
            }

            Spacer(Modifier.weight(1f))

            if (callState != CallState.CONNECTING) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(bottom = 24.dp)) {
                    CallControlButton(
                        icon = if (isMuted) Icons.Outlined.MicOff else Icons.Outlined.Mic,
                        label = if (isMuted) "Unmute" else "Mute",
                        active = isMuted,
                        onClick = viewModel::toggleMute
                    )
                    CallControlButton(
                        icon = if (isSpeakerOn) Icons.Outlined.VolumeUp else Icons.Outlined.VolumeDown,
                        label = "Speaker",
                        active = isSpeakerOn,
                        onClick = viewModel::toggleSpeaker
                    )
                    if (callType == "VIDEO") {
                        CallControlButton(
                            icon = if (isVideoOn) Icons.Outlined.Videocam else Icons.Outlined.VideocamOff,
                            label = "Camera",
                            active = !isVideoOn,
                            onClick = viewModel::toggleVideo
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.endCall() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 40.dp).height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SnoutScoutColors.Error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Outlined.CallEnd, null)
                Spacer(Modifier.width(8.dp))
                Text("End Call", style = MaterialTheme.typography.labelLarge)
            }
        }

        if (showLowBalance) {
            AlertDialog(
                onDismissRequest = viewModel::dismissLowBalanceWarning,
                title = { Text("Low Balance") },
                text = { Text("You have less than 2 minutes remaining. Your call will end automatically when balance runs out.") },
                confirmButton = { TextButton(onClick = viewModel::dismissLowBalanceWarning) { Text("Continue") } },
                dismissButton = { TextButton(onClick = { viewModel.endCall() }) { Text("End Call") } }
            )
        }
    }
}

@Composable
private fun CallControlButton(icon: ImageVector, label: String, active: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp).clip(CircleShape)
                .background(if (active) Color.White else Color.White.copy(0.15f))
        ) {
            Icon(icon, contentDescription = label,
                tint = if (active) Color.Black else Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.height(4.dp))
        Text(label, color = Color.White.copy(0.7f), style = MaterialTheme.typography.labelSmall)
    }
}
