package com.example.data

import com.example.data.ai.CyberAiService
import com.example.data.db.AppDatabase
import com.example.data.db.AuthKeyEntity
import com.example.data.db.ChatMessageEntity
import com.example.data.db.ChatRoomEntity
import com.example.data.db.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.UUID

class CyberRepository(private val db: AppDatabase) {

    private val chatDao = db.chatDao()
    private val userDao = db.userDao()
    private val authDao = db.authDao()
    private val aiService = CyberAiService()

    val chatRooms: Flow<List<ChatRoomEntity>> = chatDao.getChatRooms()
    val users: Flow<List<UserEntity>> = userDao.getAllUsers()
    val authKey: Flow<AuthKeyEntity?> = authDao.getAuthKey()

    fun getMessagesForChat(chatId: String): Flow<List<ChatMessageEntity>> =
        chatDao.getMessagesForChat(chatId)

    fun getChatRoom(chatId: String): Flow<ChatRoomEntity?> =
        chatDao.getChatRoom(chatId)

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val currentRooms = chatDao.getChatRooms().firstOrNull()
        if (currentRooms.isNullOrEmpty()) {
            val rooms = listOf(
                ChatRoomEntity(
                    id = "GHOST_AI_NEXUS",
                    title = "NEXUS-9 AI Assistant",
                    isGeneralChannel = false,
                    isAiChat = true,
                    participantCount = 1,
                    unreadCount = 1,
                    lastMessage = "⚡ Квантовый модуль связи готов. Задай вопрос ИИ-нетраннеру.",
                    lastMessageTimestamp = System.currentTimeMillis()
                ),
                ChatRoomEntity(
                    id = "GENERAL_NET_01",
                    title = "🌐 Общий Канал [Sector 07]",
                    isGeneralChannel = true,
                    isAiChat = false,
                    participantCount = 20,
                    unreadCount = 3,
                    lastMessage = "@SILVERHAND: ICE пробит! Проверяйте подсеть №4",
                    lastMessageTimestamp = System.currentTimeMillis() - 100000
                ),
                ChatRoomEntity(
                    id = "KUSANAGI_09",
                    title = "Майор Кусанаги",
                    isGeneralChannel = false,
                    isAiChat = false,
                    participantCount = 2,
                    unreadCount = 0,
                    lastMessage = "Принято. Встречаемся у терминала 9 в 23:00.",
                    lastMessageTimestamp = System.currentTimeMillis() - 3600000
                ),
                ChatRoomEntity(
                    id = "SILVERHAND_SAMURAI",
                    title = "Джонни Силверхэнд",
                    isGeneralChannel = false,
                    isAiChat = false,
                    participantCount = 2,
                    unreadCount = 1,
                    lastMessage = "Проверь этот аудио-трек для вирта!",
                    lastMessageTimestamp = System.currentTimeMillis() - 7200000
                ),
                ChatRoomEntity(
                    id = "CYBER_DOC_RIPPER",
                    title = "Док Риппер (Кибернетика)",
                    isGeneralChannel = false,
                    isAiChat = false,
                    participantCount = 2,
                    unreadCount = 0,
                    lastMessage = "Твои новые импланты Sandevistan прибыли.",
                    lastMessageTimestamp = System.currentTimeMillis() - 86400000
                )
            )
            chatDao.insertChatRooms(rooms)

            // Initial Users (20 operatives)
            val initialUsers = mutableListOf<UserEntity>()
            val names = listOf(
                "Кусанаги", "Силверхэнд", "Valentine", "Док Риппер", "Нео Zero",
                "Тринити", "Морфеус", "Декстер", "Эвелин", "Джуди",
                "Панам", "Такемура", "Роуг", "Вектор", "Т-Баг",
                "Сантос", "Кейс", "Молли", "Армитаж", "Финч"
            )
            names.forEachIndexed { i, name ->
                initialUsers.add(
                    UserEntity(
                        id = "user_$i",
                        handle = "@${name.lowercase().replace(" ", "_")}",
                        callsign = name,
                        status = if (i % 3 == 0) "ONLINE" else if (i % 2 == 0) "CYBERSPACE" else "BUSY",
                        role = if (i == 0) "NETRUNNER" else if (i == 1) "OPERATIVE" else "OPERATIVE",
                        bio = "Сектор 07 // Кибердека v4.2"
                    )
                )
            }
            userDao.insertUsers(initialUsers)

            // Auth Key initial
            authDao.insertAuthKey(AuthKeyEntity())

            // Initial Messages for AI Chat
            chatDao.insertMessage(
                ChatMessageEntity(
                    chatId = "GHOST_AI_NEXUS",
                    senderId = "ai_nexus",
                    senderName = "NEXUS-9 AI",
                    messageText = "Приветствую в защищенной консоли CyberPulse! Я твой ИИ-нетраннер. Задавай любые вопросы о взломе, коде, киберпанке или тактике.",
                    isAiResponse = true
                )
            )

            // Initial Messages for General Channel
            val generalMessages = listOf(
                ChatMessageEntity(
                    chatId = "GENERAL_NET_01",
                    senderId = "user_0",
                    senderName = "Кусанаги",
                    messageText = "Внимание всем 20 оперативникам сектора. Корпоративный сканер запустил патруль.",
                    timestamp = System.currentTimeMillis() - 600000
                ),
                ChatMessageEntity(
                    chatId = "GENERAL_NET_01",
                    senderId = "user_2",
                    senderName = "Valentine",
                    messageText = "Подтверждаю. Наш Master Hook Key держит оборону. Шифрование активна.",
                    timestamp = System.currentTimeMillis() - 400000
                ),
                ChatMessageEntity(
                    chatId = "GENERAL_NET_01",
                    senderId = "user_1",
                    senderName = "Силверхэнд",
                    messageText = "ICE пробит! Проверяйте подсеть №4. Готовьте чипы памяти.",
                    timestamp = System.currentTimeMillis() - 100000
                )
            )
            chatDao.insertMessages(generalMessages)

            // Initial messages for Johnny
            chatDao.insertMessage(
                ChatMessageEntity(
                    chatId = "SILVERHAND_SAMURAI",
                    senderId = "user_1",
                    senderName = "Силверхэнд",
                    messageText = "Проверь этот аудио-трек для вирта!",
                    messageType = "AUDIO",
                    audioDurationMs = 14000,
                    audioUri = "cyber_audio_sample",
                    timestamp = System.currentTimeMillis() - 7200000
                )
            )
        }
    }

    suspend fun sendMessage(
        chatId: String,
        text: String,
        messageType: String = "TEXT",
        imageUri: String = "",
        audioUri: String = "",
        audioDurationMs: Int = 0
    ) = withContext(Dispatchers.IO) {
        val currentAuth = authDao.getAuthKey().firstOrNull() ?: AuthKeyEntity()
        val userHandle = currentAuth.userHandle.ifBlank { "@OPERATIVE_01" }

        val userMessage = ChatMessageEntity(
            chatId = chatId,
            senderId = "current_user",
            senderName = currentAuth.callsign,
            senderAvatar = "",
            messageText = text,
            messageType = messageType,
            timestamp = System.currentTimeMillis(),
            isEncrypted = currentAuth.quantumEncryptionEnabled,
            imageUri = imageUri,
            audioUri = audioUri,
            audioDurationMs = audioDurationMs
        )

        chatDao.insertMessage(userMessage)

        val displaySnippet = when (messageType) {
            "IMAGE" -> "📷 [Cyber Image Asset]"
            "AUDIO" -> "🎵 [Voice Encryption Log ${audioDurationMs / 1000}s]"
            else -> text
        }
        chatDao.updateLastMessage(chatId, "$userHandle: $displaySnippet", System.currentTimeMillis())

        // Handle AI chat response
        if (chatId == "GHOST_AI_NEXUS") {
            val aiResponseText = aiService.getAiResponse(text)
            val aiMessage = ChatMessageEntity(
                chatId = chatId,
                senderId = "ai_nexus",
                senderName = "NEXUS-9 AI",
                messageText = aiResponseText,
                messageType = "TEXT",
                timestamp = System.currentTimeMillis(),
                isEncrypted = true,
                isAiResponse = true
            )
            chatDao.insertMessage(aiMessage)
            chatDao.updateLastMessage(chatId, "NEXUS-9: ${aiResponseText.take(60)}...", System.currentTimeMillis())
        } else if (chatId == "GENERAL_NET_01") {
            // Trigger simulated cyber response in general chat
            simulateGeneralChatReply(text)
        }
    }

    private suspend fun simulateGeneralChatReply(userMessage: String) {
        val replies = listOf(
            Pair("Кусанаги", "Принято в секторе 07. Канал очищен от корпоративного считывателя."),
            Pair("Valentine", "Зафиксировал передачу. Логи отправлены в квантовый сейф."),
            Pair("Тринити", "Отличный ход. Готовим выгрузку на терминал №9.")
        )
        val reply = replies.random()
        val simulatedMessage = ChatMessageEntity(
            chatId = "GENERAL_NET_01",
            senderId = "simulated_${reply.first}",
            senderName = reply.first,
            messageText = reply.second,
            timestamp = System.currentTimeMillis() + 500
        )
        chatDao.insertMessage(simulatedMessage)
        chatDao.updateLastMessage("GENERAL_NET_01", "${reply.first}: ${reply.second}", System.currentTimeMillis())
    }

    suspend fun authenticateWithHookKey(key: String): Boolean = withContext(Dispatchers.IO) {
        val current = authDao.getAuthKey().firstOrNull() ?: AuthKeyEntity()
        val updated = current.copy(
            hookKey = key,
            isAuthenticated = true,
            userHandle = "@OPERATIVE_${key.hashCode().toString().takeLast(3)}"
        )
        authDao.insertAuthKey(updated)
        true
    }

    suspend fun updateEncryptionSettings(quantum: Boolean, stealth: Boolean) = withContext(Dispatchers.IO) {
        val current = authDao.getAuthKey().firstOrNull() ?: AuthKeyEntity()
        val updated = current.copy(
            quantumEncryptionEnabled = quantum,
            stealthModeEnabled = stealth
        )
        authDao.insertAuthKey(updated)
    }

    suspend fun toggleBlockUser(userId: String, isBlocked: Boolean) = withContext(Dispatchers.IO) {
        userDao.updateUserBlock(userId, isBlocked)
    }

    suspend fun clearChatHistory(chatId: String) = withContext(Dispatchers.IO) {
        chatDao.clearChatMessages(chatId)
    }
}
