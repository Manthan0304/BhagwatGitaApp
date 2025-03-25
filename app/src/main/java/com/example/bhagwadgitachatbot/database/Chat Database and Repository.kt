package com.example.bhagwadgitachatbot.database

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

class ChatRepository(context: Context) {
    private val database = AppDatabase.getDatabase(context)


    private val chatDao = database.chatDao()
    private val messageDao = database.messageDao()

    // Chat operations
    fun getAllChats(): Flow<List<ChatEntity>> = chatDao.getAllChats()

    suspend fun insertChat(chat: ChatEntity): Long = chatDao.insertChat(chat)

    suspend fun deleteChat(chat: ChatEntity) = chatDao.deleteChat(chat)

    suspend fun deleteAllChats() = chatDao.deleteAllChats()

    suspend fun updateChat(chatId: Int, lastMessage: String) = chatDao.updateChat(chatId, lastMessage)

    // Message operations
    fun getMessagesForChat(chatId: Int): Flow<List<MessageEntity>> = messageDao.getMessagesForChat(chatId)

    suspend fun insertMessage(message: MessageEntity) = messageDao.insertMessage(message)

    suspend fun deleteMessage(message: MessageEntity) = messageDao.deleteMessage(message)

    suspend fun deleteAllMessages() = messageDao.deleteAllMessages()
}