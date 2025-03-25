package com.example.bhagwadgitachatbot.ui.components

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bhagwadgitachatbot.viewmodel.SettingsViewModel
import com.example.bhagwadgitachatbot.viewmodel.SettingsViewModelFactory

@Composable
fun DynamicText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    fontWeight: FontWeight? = null,
    color: Color = Color.Unspecified,
    fontFamily: FontFamily? = null,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    
    val viewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = SettingsViewModelFactory(context.applicationContext)
    )
    
    val fontSize by viewModel.fontSize.collectAsState()
    
    Log.d("DynamicText", "Current font size: $fontSize")
    
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            fontSize = fontSize.sp,
            fontWeight = fontWeight ?: style.fontWeight,
            color = if (color != Color.Unspecified) color else style.color,
            fontFamily = fontFamily ?: style.fontFamily
        )
    )
} 