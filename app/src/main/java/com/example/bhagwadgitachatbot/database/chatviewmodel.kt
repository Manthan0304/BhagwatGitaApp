package com.example.bhagwadgitachatbot.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bhagwadgitachatbot.data.*
import com.example.bhagwadgitachatbot.screens.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val chatDao = database.chatDao()
    private val messageDao = database.messageDao()

    private val _chatsState = MutableStateFlow<List<ChatEntity>>(emptyList())
    val chatsState: StateFlow<List<ChatEntity>> = _chatsState.asStateFlow()

    private val _currentChatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val currentChatMessages: StateFlow<List<ChatMessage>> = _currentChatMessages.asStateFlow()

    private var _currentChatId = MutableStateFlow<Int?>(null)
    val currentChatId: StateFlow<Int?> = _currentChatId.asStateFlow()

    private var _justCreatedNewChat = MutableStateFlow(false)
    val justCreatedNewChat: StateFlow<Boolean> = _justCreatedNewChat.asStateFlow()

    init {
        // Observe all chats
        viewModelScope.launch {
            chatDao.getAllChats().collect { chats ->
                _chatsState.value = chats
            }
        }
    }

    fun createNewChat(firstMessage: String = "New Conversation") = viewModelScope.launch {
        try {
            // Create a single chat with the first message
            val chatId = chatDao.insertChat(
                ChatEntity(
                    firstMessage = firstMessage,
                    lastMessage = firstMessage
                )
            ).toInt()
            
            // Update the current chat ID
            _currentChatId.value = chatId
            
            // Add the first message to the chat
            messageDao.insertMessage(
                MessageEntity(
                    chatId = chatId,
                    content = firstMessage,
                    isUser = false
                )
            )

            // Set the flag to indicate a new chat was just created
            _justCreatedNewChat.value = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadChat(chatId: Int) = viewModelScope.launch {
        _currentChatId.value = chatId
        _justCreatedNewChat.value = false  // Reset the flag when loading an existing chat

        // Fetch and convert messages for this chat
        messageDao.getMessagesForChat(chatId).collect { messages ->
            _currentChatMessages.value = messages.map { messageEntity ->
                ChatMessage(
                    content = messageEntity.content,
                    isUser = messageEntity.isUser
                )
            }
        }
    }

    fun saveMessage(message: ChatMessage) = viewModelScope.launch {
        val currentChatId = _currentChatId.value ?: return@launch

        // Insert message to database
        messageDao.insertMessage(
            MessageEntity(
                chatId = currentChatId,
                content = message.content,
                isUser = message.isUser
            )
        )

        // Update chat's last message
        if (message.isUser) {
            chatDao.updateChat(
                chatId = currentChatId,
                lastMessage = message.content
            )
        }

        // Optionally, update the current chat messages state
        _currentChatMessages.value += message
    }

    // Method to restore existing chat or create new one
    fun initializeChat(chatId: Int? = null) = viewModelScope.launch {
        if (chatId != null) {
            // Load existing chat
            loadChat(chatId)
        } else if (!_justCreatedNewChat.value) {
            // Create new chat only if one wasn't just created
            createNewChat()
        }
    }

    fun deleteAllChats() = viewModelScope.launch {
        try {
            withContext(Dispatchers.IO) {
                // Delete all messages first due to foreign key constraint
                messageDao.deleteAllMessages()
                // Then delete all chats
                chatDao.deleteAllChats()
                // Reset the chat sequence so new chats start from 1 again
                chatDao.resetChatSequence()
            }
            
            // Update states in the main thread
            _chatsState.value = emptyList()
            _currentChatMessages.value = emptyList()
            _currentChatId.value = null
            _justCreatedNewChat.value = false
        } catch (e: Exception) {
            e.printStackTrace()
            // You might want to handle the error or propagate it to the UI
        }
    }
}