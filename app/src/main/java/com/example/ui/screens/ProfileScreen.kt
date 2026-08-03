package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.db.AuthKeyEntity
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberGlassCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberPinkBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonFuchsia
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonYellow

@Composable
fun ProfileScreen(
    authKey: AuthKeyEntity?,
    onStatusNotification: (String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf("ONLINE") }
    val handle = authKey?.userHandle ?: "@OPERATIVE_01"
    val callsign = authKey?.callsign ?: "V-Netrunner"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ПРОФИЛЬ ОПЕРАТИВНИКА // PROFILE CARD",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp,
                color = NeonCyan
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Avatar Header Card
        CyberGlassCard(
            borderColor = NeonFuchsia,
            backgroundColor = CyberSurface
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(CyberSurfaceVariant)
                        .border(2.dp, Brush.linearGradient(listOf(NeonCyan, NeonFuchsia)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cyber_app_icon_1785742227839),
                        contentDescription = "Operative Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = callsign,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = CyberTextPrimary
                    )
                )

                Text(
                    text = handle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        color = NeonCyan
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CyberBadge(text = "NETRUNNER LEVEL 4", color = NeonFuchsia)
                    CyberBadge(text = "SECTOR 07", color = NeonGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Selector Card
        CyberGlassCard(
            borderColor = CyberBorder,
            backgroundColor = CyberSurface
        ) {
            Text(
                text = "СТАТУС В СЕТИ CYBERPULSE:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    color = CyberTextSecondary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("ONLINE", "CYBERSPACE", "BUSY").forEach { status ->
                    val isSel = selectedStatus == status
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NeonCyan.copy(alpha = 0.2f) else CyberCardBg)
                            .border(1.dp, if (isSel) NeonCyan else CyberBorder, RoundedCornerShape(8.dp))
                            .clickable {
                                selectedStatus = status
                                onStatusNotification("СТАТУС ОБНОВЛЕН: $status")
                            }
                            .padding(vertical = 10.dp)
                            .testTag("status_option_$status"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) NeonCyan else CyberTextSecondary
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hardware Specs Card
        CyberGlassCard(
            borderColor = CyberPinkBorder,
            backgroundColor = CyberSurface
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = "Cyberware",
                    tint = NeonFuchsia,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "КИБЕРНЕКТИЧЕСКИЕ ИМПЛАНТЫ:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CyberTextPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SpecItem(label = "Нейродека:", value = "Militech Paraline MK.4")
                SpecItem(label = "Шифровальщик:", value = "Quantum AES-256 Module")
                SpecItem(label = "Протокол ICE:", value = "Black-ICE Defense v8.1")
                SpecItem(label = "База данных:", value = "Room SQLite Local Vault")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // System Diagnostic Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CyberSurfaceVariant)
                .border(1.dp, NeonCyan, RoundedCornerShape(8.dp))
                .clickable {
                    onStatusNotification("ДИАГНОСТИКА: ВСЕ 20 КАНАЛОВ РАБОТАЮТ БЕЗ СБОЕВ // PING 12ms")
                }
                .padding(vertical = 12.dp)
                .testTag("profile_diagnostic_button"),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.NetworkCheck,
                    contentDescription = "Test Network",
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ЗАПУСТИТЬ ДИАГНОСТИКУ СЕТИ СЕКТОРА 07",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                )
            }
        }
    }
}

@Composable
private fun SpecItem(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                color = CyberTextSecondary
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = NeonCyan
            )
        )
    }
}
