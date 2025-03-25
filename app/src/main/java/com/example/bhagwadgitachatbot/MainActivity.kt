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
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.bhagwadgitachatbot.screens.ChatScreen
import com.example.bhagwadgitachatbot.screens.LoginScreen
import com.example.bhagwadgitachatbot.ui.theme.BhagwadgitachatbotTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.bhagwadgitachatbot.navigation.RootNavGraph
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private val viewModel: chatviewmodel by viewModels()
    private val googleauthuiclient by lazy {
        googleauthuiclient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
            viewmodel = viewModel
        )
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val navController = rememberNavController()
            val currentUser = Firebase.auth.currentUser
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = false
                )
                systemUiController.setNavigationBarColor(color = Color.Transparent)
            }

            BhagwadgitachatbotTheme {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleauthuiclient.signinwithintent(
                                    result.data ?: return@launch
                                )
                                if (signInResult) {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                        }
                    }
                )

                LaunchedEffect(Unit) {
                    if (googleauthuiclient.getSignedInUser() != null) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                LaunchedEffect(currentUser) {
                    currentUser?.uid?.let { userId ->
                        viewModel.getUserData(userId)
                    }
                }

                RootNavGraph(
                    navController = navController,
                    onSignInClick = {
                        lifecycleScope.launch {
                            val signInIntentSender = googleauthuiclient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                )
            }
        }
    }
}

