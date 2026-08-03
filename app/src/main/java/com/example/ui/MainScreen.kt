package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.components.CyberHeader
import com.example.ui.components.CyberNavBar
import com.example.ui.components.CyberNotificationBanner
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.ChatListScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SecurityScreen
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberSurfaceVariant

@Composable
fun MainScreen(
    viewModel: CyberViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    if (!uiState.isAuthenticated) {
        AuthScreen(
            onAuthenticate = { hookKey ->
                viewModel.authenticateWithHookKey(hookKey)
            }
        )
        return
    }

    val ambientBackground = Brush.radialGradient(
        colors = listOf(CyberSurfaceVariant, CyberBackground),
        radius = 1800f
    )

    Scaffold(
        topBar = {
            CyberHeader(
                title = "CORTEX-7 LINK",
                statusText = "NEURAL SESSION // 20 OPERATIVES",
                onVaultClick = { viewModel.selectTab(CyberTab.VAULT) }
            )
        },
        bottomBar = {
            if (uiState.activeChatId == null) {
                CyberNavBar(
                    selectedTab = uiState.selectedTab,
                    onTabSelected = { tab -> viewModel.selectTab(tab) }
                )
            }
        },
        containerColor = CyberBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ambientBackground)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                CyberNotificationBanner(message = uiState.statusNotification)

                if (uiState.activeChatId != null) {
                    ChatDetailScreen(
                        chatRoom = uiState.activeChatRoom,
                        messages = uiState.activeMessages,
                        isAiThinking = uiState.isAiThinking,
                        isRecordingAudio = uiState.isRecordingAudio,
                        recordingDurationSec = uiState.recordedAudioDurationSec,
                        onBackClick = { viewModel.closeChat() },
                        onSendMessage = { text, imageUri ->
                            viewModel.sendMessage(text, imageUri = imageUri)
                        },
                        onToggleAudioRecording = { viewModel.toggleAudioRecording() },
                        onClearChat = { viewModel.clearActiveChat() }
                    )
                } else {
                    when (uiState.selectedTab) {
                        CyberTab.CHATS -> {
                            ChatListScreen(
                                chatRooms = uiState.chatRooms,
                                searchQuery = uiState.searchQuery,
                                onSearchQueryChange = { q -> viewModel.setSearchQuery(q) },
                                onChatSelect = { id -> viewModel.openChat(id) }
                            )
                        }
                        CyberTab.GENERAL_CHANNEL -> {
                            ChatDetailScreen(
                                chatRoom = uiState.chatRooms.find { it.id == "GENERAL_NET_01" },
                                messages = uiState.activeMessages,
                                isAiThinking = false,
                                isRecordingAudio = uiState.isRecordingAudio,
                                recordingDurationSec = uiState.recordedAudioDurationSec,
                                onBackClick = { viewModel.selectTab(CyberTab.CHATS) },
                                onSendMessage = { text, imageUri ->
                                    viewModel.sendMessage(text, imageUri = imageUri)
                                },
                                onToggleAudioRecording = { viewModel.toggleAudioRecording() },
                                onClearChat = { viewModel.clearActiveChat() }
                            )
                        }
                        CyberTab.AI_NEXUS -> {
                            ChatDetailScreen(
                                chatRoom = uiState.chatRooms.find { it.id == "GHOST_AI_NEXUS" },
                                messages = uiState.activeMessages,
                                isAiThinking = uiState.isAiThinking,
                                isRecordingAudio = uiState.isRecordingAudio,
                                recordingDurationSec = uiState.recordedAudioDurationSec,
                                onBackClick = { viewModel.selectTab(CyberTab.CHATS) },
                                onSendMessage = { text, imageUri ->
                                    viewModel.sendMessage(text, imageUri = imageUri)
                                },
                                onToggleAudioRecording = { viewModel.toggleAudioRecording() },
                                onClearChat = { viewModel.clearActiveChat() }
                            )
                        }
                        CyberTab.VAULT -> {
                            SecurityScreen(
                                authKey = uiState.authKey,
                                users = uiState.users,
                                onToggleQuantumEncryption = { enabled ->
                                    viewModel.toggleQuantumEncryption(enabled)
                                },
                                onToggleStealthMode = { enabled ->
                                    viewModel.toggleStealthMode(enabled)
                                },
                                onToggleBlockUser = { userId, blocked ->
                                    viewModel.toggleUserBlock(userId, blocked)
                                }
                            )
                        }
                        CyberTab.PROFILE -> {
                            ProfileScreen(
                                authKey = uiState.authKey,
                                onStatusNotification = { msg ->
                                    viewModel.showNotification(msg)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
