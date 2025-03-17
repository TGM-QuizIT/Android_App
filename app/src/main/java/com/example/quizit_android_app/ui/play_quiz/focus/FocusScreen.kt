package com.example.quizit_android_app.ui.play_quiz.focus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import coil3.compose.AsyncImage
import com.example.quizit_android_app.R
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.network.NetworkMonitor
import com.example.quizit_android_app.ui.home.NoContentPlaceholder
import com.example.quizit_android_app.ui.theme.Typography
import com.example.quizit_android_app.usecases.focus.GetFocusForSubjectUseCase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FocusScreen(
    navigateToQuiz: (Subject, Focus?) -> Unit,
    navigateBack: () -> Unit,
    focusViewModel: FocusViewModel = hiltViewModel(),
    networkMonitor: NetworkMonitor = hiltViewModel(),
    navigateToQuizDetail: (Subject, Focus?) -> Unit,
) {

    val isConnected = networkMonitor.isConnected

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    val focusList = focusViewModel.focusList
    val overallQuestionCount = focusViewModel.overallQuestionCount
    val isLoading = focusViewModel.isLoading
    val subject = focusViewModel.subject

    var isRefreshing by remember { mutableStateOf(false)}

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            focusViewModel.refreshData {
                isRefreshing = false
            }
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                IconButton(
                    onClick = { navigateBack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = Color(0xFF8F9098)
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Schwerpunkte",
                            style = Typography.titleLarge
                        )
                        Text(
                            text = subject?.subjectName ?: "",
                            style = Typography.titleLarge
                        )
                    }
                }
            }
        },

        content = { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                if(isLoading) {
                    CircularProgressIndicator( modifier = Modifier.align(Alignment.Center), trackColor = Color.Gray)
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    ) {



                        LazyColumn {

                            if(focusList.isEmpty()) {
                                item {
                                    NoContentPlaceholder(id = R.drawable.no_focus_placeholder)

                                }
                            } else {

                                item {
                                    FocusCard(
                                        type = CardType.Subject,
                                        subject = subject!!,
                                        focus = null,
                                        overallQuestionCount = overallQuestionCount,
                                        onQuizStart = { subject, focus ->

                                            if(isConnected) {
                                                navigateToQuiz(subject, focus)
                                            } else {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Keine Internetverbindung", "OK", duration = SnackbarDuration.Short)
                                                }
                                            }
                                        },
                                        navigateToQuizDetail = {  subject, focus ->
                                            navigateToQuizDetail(subject, focus)
                                        }
                                    )
                                }

                                items(focusList) {
                                    FocusCard(
                                        type = CardType.Focus,
                                        focus = it,
                                        subject = subject!!,
                                        overallQuestionCount = overallQuestionCount,
                                        onQuizStart = {subject, focus ->
                                            if(isConnected) {
                                                navigateToQuiz(subject, focus)
                                            } else {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Keine Internetverbindung", "OK", duration = SnackbarDuration.Short)
                                                }
                                            }
                                        },
                                        navigateToQuizDetail = {  subject, focus ->
                                            navigateToQuizDetail(subject, focus)
                                        }
                                    )
                                }

                                item { Spacer(modifier = Modifier.height(16.dp)) }
                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

        }
    )
}

@Composable
fun FocusCard(
    type: CardType,
    subject: Subject,
    focus: Focus?,
    onQuizStart: (Subject, Focus?) -> Unit,
    overallQuestionCount: Int,
    navigateToQuizDetail: (Subject, Focus?) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp,)
            .clickable { navigateToQuizDetail(subject, focus) },
        colors = CardDefaults.cardColors(
            containerColor = if (type == CardType.Subject) Color(0xFFEAF2FF) else Color(0xFFF8F9FE),
            contentColor = Color.Black
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 16.dp, bottom = 8.dp, end = 16.dp)
                .wrapContentHeight(Alignment.CenterVertically),
        ) {
            Text(
                text = when (type) {
                    CardType.Subject -> subject?.subjectName ?: ""
                    CardType.Focus -> focus?.focusName ?: ""
                },
                style = MaterialTheme.typography.titleMedium,

                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1 // Der Titel bleibt in einer Zeile
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {


                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {

                    Text(
                        text = when (type) {
                            CardType.Subject -> "${overallQuestionCount} Fragen im Pool"
                            CardType.Focus -> "${focus?.questionCount ?: 0} Fragen im Pool"
                        },
                        style = Typography.labelLarge
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Button(
                        onClick = { ->
                            when (type) {
                                CardType.Subject -> subject?.subjectId?.let { onQuizStart(subject, null) }
                                CardType.Focus -> focus?.focusId?.let { onQuizStart(subject, focus) }
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),

                        ) {
                        Text(
                            "Quiz starten",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }

                AsyncImage(

                    model = when (type) {

                        CardType.Subject -> subject.subjectImageAddress
                        CardType.Focus -> focus?.focusImageAddress
                    },
                    contentDescription = "Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 1f)
                        .weight(1f)
                        .align(Alignment.CenterVertically)// Größe des Bildes
                        .clip(RoundedCornerShape(8.dp))
                )





            }

        }


    }
}

enum class CardType {
    Subject,
    Focus
}
