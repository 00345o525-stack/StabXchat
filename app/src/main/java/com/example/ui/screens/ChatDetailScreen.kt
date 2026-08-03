package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.db.ChatMessageEntity
import com.example.data.db.ChatRoomEntity
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberGlassCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberPinkBorder
import com.example.ui.theme.CyberPurpleBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonFuchsia
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatDetailScreen(
    chatRoom: ChatRoomEntity?,
    messages: List<ChatMessageEntity>,
    isAiThinking: Boolean,
    isRecordingAudio: Boolean,
    recordingDurationSec: Int,
    onBackClick: () -> Unit,
    onSendMessage: (text: String, imageUri: String) -> Unit,
    onToggleAudioRecording: () -> Unit,
    onClearChat: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var selectedImageAttachment by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val title = chatRoom?.title ?: "Cyber Channel"
    val isAiChat = chatRoom?.isAiChat ?: false
    val isGeneral = chatRoom?.isGeneralChannel ?: false

    val aiQuickPrompts = listOf(
        "⚡ Проверить статус Black-ICE",
        "☣️ Написать скрипт квантового шифра",
        "🌐 Кто сейчас в сети 20 оперативников?",
        "🔍 Аудит безопасности сектора 07"
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
            .imePadding()
    ) {
        // Chat Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CyberSurface)
                .border(1.dp, CyberBorder, RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("chat_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonCyan
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (isAiChat) NeonFuchsia else CyberTextPrimary
                                )
                            )
                            if (isAiChat) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI",
                                    tint = NeonFuchsia,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = if (isAiChat) "NEXUS-9 // GEMINI NETRUNNER ENGINE" else if (isGeneral) "ОБЩИЙ КАНАЛ СВЯЗИ (20 ОПЕРАТИВНИКОВ)" else "КВАНТОВЫЙ ЧАТ AES-256",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = CyberTextSecondary
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClearChat) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear Chat",
                            tint = CyberTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    CyberBadge(text = "AES-256", color = NeonGreen)
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            items(messages) { msg ->
                MessageBubbleItem(msg = msg, isAiChat = isAiChat)
            }

            if (isAiThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        CircularProgressIndicator(
                            color = NeonFuchsia,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NEXUS-9 AI ДЕКОДИРУЕТ ДАННЫЕ...",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                color = NeonFuchsia
                            )
                        )
                    }
                }
            }
        }

        // Quick AI Prompts
        if (isAiChat) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                items(aiQuickPrompts) { prompt ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(CyberSurfaceVariant)
                            .border(1.dp, CyberPinkBorder, RoundedCornerShape(16.dp))
                            .clickable { onSendMessage(prompt, "") }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = prompt,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = NeonFuchsia
                            )
                        )
                    }
                }
            }
        }

        // Selected Image Preview Attachment Banner
        AnimatedVisibility(visible = selectedImageAttachment.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.cyber_hero_banner_1785742242627),
                        contentDescription = "Attachment",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "📷 Прикреплен Cyber Visual Asset",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            color = NeonCyan
                        )
                    )
                }

                Text(
                    text = "УДАЛИТЬ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = NeonFuchsia,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { selectedImageAttachment = "" }
                )
            }
        }

        // Audio Recording Indicator Banner
        AnimatedVisibility(visible = isRecordingAudio) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NeonFuchsia.copy(alpha = 0.2f))
                    .border(1.dp, NeonFuchsia)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "Audio Recording",
                        tint = NeonFuchsia,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🎤 ИДЕТ ЗАПИСЬ ГОЛОСА: ${recordingDurationSec}s",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonFuchsia
                        )
                    )
                }

                Text(
                    text = "ОТПРАВИТЬ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    ),
                    modifier = Modifier
                        .clickable { onToggleAudioRecording() }
                        .padding(horizontal = 8.dp)
                )
            }
        }

        // Sub-action shortcuts banner
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberSurfaceVariant)
                    .border(1.dp, CyberPurpleBorder, RoundedCornerShape(8.dp))
                    .clickable { selectedImageAttachment = "cyber_hero_banner_1785742242627" }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "+ FILE_ENC",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = NeonCyan
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberSurfaceVariant)
                    .border(1.dp, CyberPurpleBorder, RoundedCornerShape(8.dp))
                    .clickable { onToggleAudioRecording() }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "VOICE_MOD",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = NeonFuchsia
                    )
                )
            }
        }

        // Input Field & Action Controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CyberSurface)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.3f), NeonFuchsia.copy(alpha = 0.3f))),
                    shape = androidx.compose.ui.graphics.RectangleShape
                )
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Attach Cyber Image Button
                IconButton(
                    onClick = {
                        selectedImageAttachment = "cyber_hero_banner_1785742242627"
                    },
                    modifier = Modifier.testTag("attach_image_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Attach Image",
                        tint = if (selectedImageAttachment.isNotEmpty()) NeonCyan else CyberTextSecondary
                    )
                }

                // Text Input
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "// Enter command or neural query...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = CyberTextSecondary
                            )
                        )
                    },
                    singleLine = false,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedContainerColor = CyberCardBg,
                        unfocusedContainerColor = CyberCardBg
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("message_input_field")
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Audio Voice Note Button
                IconButton(
                    onClick = onToggleAudioRecording,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isRecordingAudio) NeonFuchsia else CyberSurfaceVariant)
                        .testTag("audio_record_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Record Audio",
                        tint = if (isRecordingAudio) Color.White else NeonCyan
                    )
                }

                // Send Button
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank() || selectedImageAttachment.isNotEmpty()) {
                            onSendMessage(inputText, selectedImageAttachment)
                            inputText = ""
                            selectedImageAttachment = ""
                        }
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(NeonFuchsia)
                        .testTag("send_message_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = NeonCyan
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubbleItem(msg: ChatMessageEntity, isAiChat: Boolean) {
    val isMe = msg.senderId == "current_user"
    val isAi = msg.isAiResponse

    val align = if (isMe) Alignment.End else Alignment.Start
    val bubbleBg = when {
        isMe -> CyberCardBg
        isAi -> CyberSurfaceVariant
        else -> CyberSurfaceVariant
    }

    val headerTag = when {
        isMe -> "OPERATOR // USER_01"
        isAi -> "SYSTEM // NEURAL_LINK"
        else -> msg.senderName.uppercase()
    }

    val headerColor = when {
        isMe -> NeonFuchsia
        isAi -> NeonCyan
        else -> CyberTextSecondary
    }

    val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(msg.timestamp))

    Column(
        horizontalAlignment = align,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("message_bubble_${msg.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 2.dp)
        ) {
            Text(
                text = headerTag,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = headerColor
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    color = CyberTextSecondary
                )
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(bubbleBg)
                .then(
                    if (isMe) {
                        Modifier.border(
                            width = 1.dp,
                            color = NeonFuchsia.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = NeonCyan.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                )
                .padding(12.dp)
        ) {
            Column {
                if (msg.imageUri.isNotEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.cyber_hero_banner_1785742242627),
                        contentDescription = "Attached Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .padding(bottom = 8.dp)
                    )
                }

                if (msg.messageType == "AUDIO") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberSurface)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Audio",
                            tint = NeonFuchsia,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Waveform",
                            tint = NeonCyan,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${msg.audioDurationMs / 1000}s",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = CyberTextPrimary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = msg.messageText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        color = if (isAi) NeonCyan else CyberTextPrimary,
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encrypted",
                        tint = NeonGreen,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "CIPHER: ${msg.cipherHash}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            color = NeonGreen
                        )
                    )
                }
            }
        }
    }
}
