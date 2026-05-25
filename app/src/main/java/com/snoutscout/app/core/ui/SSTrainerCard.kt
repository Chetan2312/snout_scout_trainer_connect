package com.snoutscout.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.data.model.TrainerProfile

@Composable
fun SSTrainerCard(
    trainer: TrainerProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SSCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box {
                SSAvatar(
                    initials = trainer.name.split(" ").take(2).joinToString("") { it.first().toString() },
                    size = 52
                )
                if (trainer.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(SnoutScoutColors.Success)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(trainer.name, style = MaterialTheme.typography.titleMedium)
                    if (trainer.isVerified) {
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Verified",
                            tint = SnoutScoutColors.Primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    "${trainer.city} · ${trainer.experience}y exp",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(trainer.specializations.take(3)) { spec ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(SnoutScoutColors.Primary.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(spec, style = MaterialTheme.typography.labelSmall, color = SnoutScoutColors.Primary)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SSRating(trainer.rating, showCount = true, count = trainer.reviewCount)
                    Spacer(Modifier.weight(1f))
                    Text(
                        "₹${trainer.ratePerMin}/min",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
