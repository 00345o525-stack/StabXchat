package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CyberRepository
import com.example.data.db.AppDatabase
import com.example.data.db.AuthKeyEntity
import com.example.data.db.ChatMessageEntity
import com.example.data.db.ChatRoomEntity
import com.example.data.db.UserEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class CyberTab {
    CHATS, GENERAL_CHANNEL, AI_NEXUS, VAULT, PROFILE
}

data class CyberUiState(
    val isAuthenticated: Boolean = true,
    val authKey: AuthKeyEntity? = null,
    val chatRooms: List<ChatRoomEntity> = emptyList(),
    val users: List<UserEntity> = emptyList(),
    val activeChatId: String? = null,
    val activeChatRoom: ChatRoomEntity? = null,
    val activeMessages: List<ChatMessageEntity> = emptyList(),
    val isAiThinking: Boolean = false,
    val isRecordingAudio: Boolean = false,
    val recordedAudioDurationSec: Int = 0,
    val selectedTab: CyberTab = CyberTab.CHATS,
    val statusNotification: String? = null,
    val searchQuery: String = ""
)

class CyberViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CyberRepository
    private val _uiState = MutableStateFlow(CyberUiState())
    val uiState: StateFlow<CyberUiState> = _uiState.asStateFlow()

    private var audioRecordingJob: Job? = null
    private var activeMessageJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = CyberRepository(db)

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        viewModelScope.launch {
            repository.chatRooms.collectLatest { rooms ->
                _uiState.update { it.copy(chatRooms = rooms) }
            }
        }

        viewModelScope.launch {
            repository.users.collectLatest { users ->
                _uiState.update { it.copy(users = users) }
            }
        }

        viewModelScope.launch {
            repository.authKey.collectLatest { key ->
                _uiState.update {
                    it.copy(
                        authKey = key,
                        isAuthenticated = key?.isAuthenticated ?: true
                    )
                }
            }
        }
    }

    fun selectTab(tab: CyberTab) {
        _uiState.update { it.copy(selectedTab = tab, activeChatId = null) }
        if (tab == CyberTab.GENERAL_CHANNEL) {
            openChat("GENERAL_NET_01")
        } else if (tab == CyberTab.AI_NEXUS) {
            openChat("GHOST_AI_NEXUS")
        }
    }

    fun openChat(chatId: String) {
        activeMessageJob?.cancel()
        _uiState.update { it.copy(activeChatId = chatId) }

        activeMessageJob = viewModelScope.launch {
            repository.getMessagesForChat(chatId).collectLatest { msgs ->
                _uiState.update { state ->
                    state.copy(
                        activeMessages = msgs,
                        activeChatRoom = state.chatRooms.find { it.id == chatId }
                    )
                }
            }
        }
    }

    fun closeChat() {
        activeMessageJob?.cancel()
        _uiState.update { it.copy(activeChatId = null, activeChatRoom = null) }
    }

    fun sendMessage(text: String, imageUri: String = "", audioUri: String = "", audioDurationMs: Int = 0) {
        val chatId = _uiState.value.activeChatId ?: return
        if (text.isBlank() && imageUri.isBlank() && audioUri.isBlank()) return

        if (chatId == "GHOST_AI_NEXUS") {
            _uiState.update { it.copy(isAiThinking = true) }
        }

        viewModelScope.launch {
            repository.sendMessage(
                chatId = chatId,
                text = text,
                imageUri = imageUri,
                audioUri = audioUri,
                audioDurationMs = audioDurationMs
            )
            _uiState.update { it.copy(isAiThinking = false) }
        }
    }

    fun toggleAudioRecording() {
        if (_uiState.value.isRecordingAudio) {
            // Stop recording & send audio message
            val duration = _uiState.value.recordedAudioDurationSec
            audioRecordingJob?.cancel()
            _uiState.update { it.copy(isRecordingAudio = false, recordedAudioDurationSec = 0) }

            if (duration >= 1) {
                sendMessage(
                    text = "🎤 [Голосовой модуль $duration сек]",
                    audioUri = "cyber_voice_note_simulated",
                    audioDurationMs = duration * 1000
                )
                showNotification("Голосовая запись зашифрована и отправлена (${duration}s)")
            }
        } else {
            // Start recording
            _uiState.update { it.copy(isRecordingAudio = true, recordedAudioDurationSec = 0) }
            audioRecordingJob = viewModelScope.launch {
                while (_uiState.value.isRecordingAudio) {
                    delay(1000)
                    _uiState.update { it.copy(recordedAudioDurationSec = it.recordedAudioDurationSec + 1) }
                }
            }
        }
    }

    fun authenticateWithHookKey(key: String) {
        viewModelScope.launch {
            val success = repository.authenticateWithHookKey(key)
            if (success) {
                showNotification("ВХОД УСПЕШЕН // КЛЮЧ-КРЮЧОК АКТИВИРОВАН")
            }
        }
    }

    fun toggleQuantumEncryption(enabled: Boolean) {
        viewModelScope.launch {
            val currentStealth = _uiState.value.authKey?.stealthModeEnabled ?: false
            repository.updateEncryptionSettings(enabled, currentStealth)
            showNotification(if (enabled) "КВАНТОВОЕ ШИФРОВАНИЕ ВКТЮЧЕНО [AES-256]" else "ШИФРОВАНИЕ ОТКЛЮЧЕНО")
        }
    }

    fun toggleStealthMode(enabled: Boolean) {
        viewModelScope.launch {
            val currentQuantum = _uiState.value.authKey?.quantumEncryptionEnabled ?: true
            repository.updateEncryptionSettings(currentQuantum, enabled)
            showNotification(if (enabled) "STEALTH РЕЖИМ АКТИВЕН // ИДО-СКРЫТИЕ" else "STEALTH РЕЖИМ ВЫКЛЮЧЕН")
        }
    }

    fun toggleUserBlock(userId: String, isBlocked: Boolean) {
        viewModelScope.launch {
            repository.toggleBlockUser(userId, isBlocked)
            showNotification(if (isBlocked) "ПОЛЬЗОВАТЕЛЬ ЗАБЛОКИРОВАН ФАЙРВОЛОМ" else "БЛОКИРОВКА СНЯТА")
        }
    }

    fun clearActiveChat() {
        val chatId = _uiState.value.activeChatId ?: return
        viewModelScope.launch {
            repository.clearChatHistory(chatId)
            showNotification("ИСТОРИЯ ЧАТА ОЧИЩЕНА")
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun showNotification(msg: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(statusNotification = msg) }
            delay(3500)
            _uiState.update { state ->
                if (state.statusNotification == msg) state.copy(statusNotification = null) else state
            }
        }
    }
}
