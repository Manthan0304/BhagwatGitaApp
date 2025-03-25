package com.example.bhagwadgitachatbot.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bhagwadgitachatbot.data.SettingsDataStore
import com.example.bhagwadgitachatbot.database.ChatViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val application: Application,
    context: Context
) : ViewModel() {
    private val settingsDataStore = SettingsDataStore(context)
    private val chatViewModel: ChatViewModel = ChatViewModel(application)
    
    private val _fontSize = MutableStateFlow(SettingsDataStore.DEFAULT_FONT_SIZE)
    val fontSize: StateFlow<Float> = _fontSize

    init {
        viewModelScope.launch {
            settingsDataStore.fontSizeFlow.collectLatest { size ->
                _fontSize.value = size
            }
        }
    }

    fun setFontSize(size: Float) {
        viewModelScope.launch {
            settingsDataStore.setFontSize(size)
        }
    }

    fun clearAllChats() {
        chatViewModel.deleteAllChats()
    }
} 