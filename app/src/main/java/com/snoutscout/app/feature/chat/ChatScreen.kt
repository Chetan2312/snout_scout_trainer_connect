package com.snoutscout.app.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.SSTopBar

private data class ChatMessage(val text: String, val isUser: Boolean, val time: String)

@Composable
fun ChatScreen(sessionId: String, onBack: () -> Unit) {
    var messages by remember {
        mutableStateOf(listOf(
            ChatMessage("Hello! I reviewed Bruno's case before our session. I can see he's been struggling with leash reactivity.", false, "10:02 AM"),
            ChatMessage("Yes, it's been quite challenging. He pulls so hard sometimes I can barely hold him.", true, "10:03 AM"),
            ChatMessage("That's very common with German Shepherds. Let's start with the LAT protocol I mentioned. The key is staying below threshold.", false, "10:05 AM"),
            ChatMessage("How far away should we be from other dogs initially?", true, "10:06 AM"),
            ChatMessage("Start at 10 meters and only move closer when Bruno can look at the trigger and look back at you calmly. It might take 2-3 weeks at each distance.", false, "10:08 AM"),
        ))
    }
    var input by remember { mutableStateOf("") }

    Scaffold(
        topBar = { SSTopBar("Chat", onBack = onBack) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                reverseLayout = false
            ) {
                items(messages) { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .widthIn(max = 260.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 12.dp, topEnd = 12.dp,
                                        bottomStart = if (msg.isUser) 12.dp else 2.dp,
                                        bottomEnd = if (msg.isUser) 2.dp else 12.dp
                                    )
                                )
                                .background(if (msg.isUser) SnoutScoutColors.Primary else MaterialTheme.colorScheme.surfaceVariant)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    msg.text,
                                    color = if (msg.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    msg.time,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (msg.isUser) MaterialTheme.colorScheme.onPrimary.copy(0.6f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = input, onValueChange = { input = it },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier.weight(1f), singleLine = true,
                    shape = RoundedCornerShape(24.dp)
                )
                IconButton(
                    onClick = {
                        if (input.isNotBlank()) {
                            messages = messages + ChatMessage(input, true, "now")
                            input = ""
                        }
                    },
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(SnoutScoutColors.Primary)
                ) {
                    Icon(Icons.Outlined.Send, "Send", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
