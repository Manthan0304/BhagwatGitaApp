package com.example.bhagwadgitachatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.bhagwadgitachatbot.screens.ChatScreen
import com.example.bhagwadgitachatbot.screens.LoginScreen
import com.example.bhagwadgitachatbot.ui.theme.BhagwadgitachatbotTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel: chatviewmodel by viewModels()
    private val googleauthuiclient by lazy {
        googleauthuiclient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
            viewmodel = viewModel
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BhagwadgitachatbotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatScreen()
//                    val launcher =
//                        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
//                            onResult = { result ->
//                                if (result.resultCode == RESULT_OK) {
//                                    lifecycleScope.launch {
//                                        val signInresult = googleauthuiclient.signinwithintent(
//                                            result.data ?: return@launch
//                                        )
//                                    }
//                                }
//                            })
//                    LoginScreen(onSignInClick = {
//                        lifecycleScope.launch {
//                            val signInIntentSender = googleauthuiclient.signIn()
//                            launcher.launch(
//                                IntentSenderRequest.Builder(
//                                    signInIntentSender ?: return@launch
//                                ).build()
//                            )
//                        }
//                    })
                }

            }
        }
    }
}

