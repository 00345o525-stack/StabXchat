package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_rooms ORDER BY lastMessageTimestamp DESC")
    fun getChatRooms(): Flow<List<ChatRoomEntity>>

    @Query("SELECT * FROM chat_rooms WHERE id = :chatId LIMIT 1")
    fun getChatRoom(chatId: String): Flow<ChatRoomEntity?>

    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRoom(room: ChatRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRooms(rooms: List<ChatRoomEntity>)

    @Query("UPDATE chat_rooms SET lastMessage = :lastMessage, lastMessageTimestamp = :timestamp WHERE id = :chatId")
    suspend fun updateLastMessage(chatId: String, lastMessage: String, timestamp: Long)

    @Query("UPDATE chat_rooms SET unreadCount = 0 WHERE id = :chatId")
    suspend fun clearUnread(chatId: String)

    @Query("DELETE FROM chat_messages WHERE chatId = :chatId")
    suspend fun clearChatMessages(chatId: String)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY handle ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("UPDATE users SET isBlocked = :isBlocked WHERE id = :userId")
    suspend fun updateUserBlock(userId: String, isBlocked: Boolean)
}

@Dao
interface AuthDao {
    @Query("SELECT * FROM auth_keys WHERE id = 1 LIMIT 1")
    fun getAuthKey(): Flow<AuthKeyEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthKey(authKey: AuthKeyEntity)

    @Update
    suspend fun updateAuthKey(authKey: AuthKeyEntity)
}
