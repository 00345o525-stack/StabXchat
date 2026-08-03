package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CyberTab
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberPinkBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonFuchsia

@Composable
fun CyberNavBar(
    selectedTab: CyberTab,
    onTabSelected: (CyberTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CyberBackground)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.5f), NeonFuchsia.copy(alpha = 0.5f))),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .navigationBarsPadding()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.Chat,
                label = "Чаты",
                isSelected = selectedTab == CyberTab.CHATS,
                testTag = "nav_chats",
                onClick = { onTabSelected(CyberTab.CHATS) }
            )
            NavItem(
                icon = Icons.Default.Public,
                label = "Общий 20/20",
                isSelected = selectedTab == CyberTab.GENERAL_CHANNEL,
                testTag = "nav_general",
                onClick = { onTabSelected(CyberTab.GENERAL_CHANNEL) }
            )
            NavItem(
                icon = Icons.Default.AutoAwesome,
                label = "NEXUS AI",
                isSelected = selectedTab == CyberTab.AI_NEXUS,
                testTag = "nav_ai_nexus",
                onClick = { onTabSelected(CyberTab.AI_NEXUS) }
            )
            NavItem(
                icon = Icons.Default.Lock,
                label = "Шифр",
                isSelected = selectedTab == CyberTab.VAULT,
                testTag = "nav_vault",
                onClick = { onTabSelected(CyberTab.VAULT) }
            )
            NavItem(
                icon = Icons.Default.Person,
                label = "Профиль",
                isSelected = selectedTab == CyberTab.PROFILE,
                testTag = "nav_profile",
                onClick = { onTabSelected(CyberTab.PROFILE) }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val activeColor = if (isSelected) NeonCyan else CyberTextSecondary
    val containerBg = if (isSelected) NeonCyan.copy(alpha = 0.12f) else Color.Transparent
    val borderStroke = if (isSelected) CyberBorder else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(containerBg)
            .border(0.8.dp, borderStroke, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = activeColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.sp,
                color = activeColor
            )
        )
    }
}
