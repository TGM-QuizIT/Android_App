package com.example.quizit_android_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.quizit_android_app.navigation.AppNavGraph
import com.example.quizit_android_app.ui.MainScreen
import com.example.quizit_android_app.ui.theme.QuizIT_Android_AppTheme
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalDoneChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalOpenChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.focus.SyncLocalFocusUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalAcceptedFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalPendingFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.result.SyncLocalResultsUseCase
import com.example.quizit_android_app.usecases.localdata.subjects.SyncLocalSubjectsUseCase
import com.example.quizit_android_app.usecases.localdata.userstats.SyncLocalUserStatsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizIT_Android_AppTheme {
                MainScreen()
            }
        }
    }

    @Inject
    lateinit var syncLocalSubjectsUseCase: SyncLocalSubjectsUseCase

    @Inject
    lateinit var syncLocalOpenChallengesUseCase: SyncLocalOpenChallengesUseCase

    @Inject
    lateinit var syncLocalDoneChallengesUseCase: SyncLocalDoneChallengesUseCase

    @Inject
    lateinit var syncLocalFocusUseCase: SyncLocalFocusUseCase

    @Inject
    lateinit var syncLocalAcceptedFriendsUseCase: SyncLocalAcceptedFriendsUseCase

    @Inject
    lateinit var syncLocalPendingFriendsUseCase: SyncLocalPendingFriendsUseCase

    @Inject
    lateinit var syncLocalResultsUseCase: SyncLocalResultsUseCase

    @Inject
    lateinit var syncLocalUserStatsUseCase: SyncLocalUserStatsUseCase

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("MainActivity", "onStart()")
            syncLocalSubjectsUseCase()
            syncLocalOpenChallengesUseCase()
            syncLocalDoneChallengesUseCase()
            syncLocalFocusUseCase()
            syncLocalAcceptedFriendsUseCase()
            syncLocalPendingFriendsUseCase()
            syncLocalResultsUseCase()
            syncLocalUserStatsUseCase()
        }
    }
}

