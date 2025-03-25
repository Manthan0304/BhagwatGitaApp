package com.example.bhagwadgitachatbot.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY id DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity): Long

    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'chats'")
    suspend fun resetChatSequence()

    @Query("UPDATE chats SET last_message = :lastMessage WHERE id = :chatId")
    suspend fun updateChat(chatId: Int, lastMessage: String)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chat_id = :chatId ORDER BY timestamp")
    fun getMessagesForChat(chatId: Int): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Delete
    suspend fun deleteMessage(message: MessageEntity)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
}