package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val handle: String,
    val callsign: String,
    val avatarUri: String = "",
    val status: String = "ONLINE", // ONLINE, CYBERSPACE, BUSY, OFFLINE
    val role: String = "OPERATIVE", // OPERATIVE, NETRUNNER, SYSADMIN, AI_NEXUS
    val isBlocked: Boolean = false,
    val bio: String = "Netrunner on Sector 07"
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String = "",
    val messageText: String,
    val messageType: String = "TEXT", // TEXT, IMAGE, AUDIO, CYBER_INTEL
    val timestamp: Long = System.currentTimeMillis(),
    val isEncrypted: Boolean = true,
    val audioUri: String = "",
    val audioDurationMs: Int = 0,
    val imageUri: String = "",
    val isAiResponse: Boolean = false,
    val cipherHash: String = "0x8F9A2B"
)

@Entity(tableName = "chat_rooms")
data class ChatRoomEntity(
    @PrimaryKey val id: String,
    val title: String,
    val isGeneralChannel: Boolean = false,
    val isAiChat: Boolean = false,
    val participantCount: Int = 2,
    val unreadCount: Int = 0,
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = System.currentTimeMillis(),
    val avatarUri: String = "",
    val isEncrypted: Boolean = true
)

@Entity(tableName = "auth_keys")
data class AuthKeyEntity(
    @PrimaryKey val id: Int = 1,
    val hookKey: String = "HOOK-NET://99A-CYBER-88B",
    val qrPayload: String = "CYBERPULSE_AUTH_KEY_SECTOR_07",
    val userHandle: String = "@CYBER_OPERATIVE_01",
    val callsign: String = "V-Netrunner",
    val isAuthenticated: Boolean = true,
    val stealthModeEnabled: Boolean = false,
    val quantumEncryptionEnabled: Boolean = true
)
