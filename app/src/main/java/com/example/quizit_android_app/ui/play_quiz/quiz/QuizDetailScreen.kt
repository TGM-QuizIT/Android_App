package com.example.quizit_android_app.ui.play_quiz.quiz


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizit_android_app.R
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import com.example.quizit_android_app.model.retrofit.Result
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.model.retrofit.User
import com.example.quizit_android_app.network.NetworkMonitor
import com.example.quizit_android_app.ui.home.ChallengeType
import com.example.quizit_android_app.ui.home.NoContentPlaceholder
import com.example.quizit_android_app.ui.home.OpenChallengeCard
import com.example.quizit_android_app.ui.social.DoneChallengeCard
import com.example.quizit_android_app.ui.theme.Typography
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun QuizDetailScreen(
    viewModel: QuizDetailViewModel = hiltViewModel(),
    networkMonitor: NetworkMonitor = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToQuiz: (Subject?, Focus?) -> Unit,
    navigateToPlayChallenge: (OpenChallenges?) -> Unit,
    navigateToUserDetail: (Int?, User) -> Unit,
) {

    val isConnected = networkMonitor.isConnected
    val snackbarHostState = remember { SnackbarHostState() }

    val subject = viewModel.subject
    val focus = viewModel.focus
    val isLoading = viewModel.isLoading
    val openChallenges = viewModel.openChallenges
    val doneChallenges = viewModel.doneChallenges
    val results = viewModel.results

    var selectedChallenge by remember { mutableStateOf<OpenChallenges?>(null) }
    val challengeSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = challengeSheetState,
        sheetContent = {
            selectedChallenge.let { challenge ->
                ChallengeBottomSheet(
                    onClose = { coroutineScope.launch { challengeSheetState.hide() } },
                    challenge = challenge,
                    onChallengeDecline = {
                        coroutineScope.launch {
                            viewModel.declineChallenge(challenge?.challengeId!!)
                            challengeSheetState.hide()
                        }
                    },
                    onChallengeAccept = {
                        coroutineScope.launch {
                            navigateToPlayChallenge(challenge)
                            challengeSheetState.hide()
                        }
                    }
                )
            } ?: Box(modifier = Modifier.size(1.dp))
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
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
                        Text(
                            text = focus?.focusName ?: subject?.subjectName ?: "",
                            style = Typography.titleLarge,
                        )
                    }
                }

            },
            content = { paddingValues ->

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            trackColor = Color.Gray
                        )


                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(paddingValues)
                                .padding(start = 16.dp, top = 16.dp)
                        ) {

                            item {
                                Text("Herausforderungen in ${subject?.subjectName}", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(16.dp))

                                if(openChallenges.isEmpty()) {
                                    NoContentPlaceholder(id = R.drawable.no_open_challenges_placeholder)

                                } else {
                                    LazyRow {
                                        items(openChallenges) { openChallenge ->

                                            if(openChallenge.friendScore?.resultScore != null)  {
                                                OpenChallengeCard(
                                                    type = ChallengeType.SUBJECT,
                                                    challenge = openChallenge,
                                                    onClick = {

                                                        if(!isConnected) {
                                                            coroutineScope.launch {
                                                                snackbarHostState.showSnackbar("Keine Internetverbindung", "OK", duration = androidx.compose.material3.SnackbarDuration.Short)
                                                            }
                                                        } else {

                                                            selectedChallenge = openChallenge
                                                            coroutineScope.launch { challengeSheetState.show() }

                                                        }
                                                    },
                                                )
                                                Spacer(modifier = Modifier.width(16.dp))
                                            }
                                        }

                                    }

                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Herausforderungen Historie", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(16.dp))

                                if(doneChallenges.isEmpty()) {
                                    NoContentPlaceholder(id = R.drawable.no_done_challenges_placeholder)

                                } else {
                                    LazyRow {
                                        items(doneChallenges) { doneChallenge ->
                                            DoneChallengeCard(
                                                challenge = doneChallenge,
                                                navigateToQuizDetail = { subject, focus ->

                                                },
                                                navigateToUserDetail = { id, user ->
                                                    navigateToUserDetail(id, user)
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                    }

                                }

                            }

                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Deine Resultate", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            if(results.isEmpty()) {
                                item {
                                    NoContentPlaceholder(id = R.drawable.no_results_placeholder)
                                }
                            } else {
                                itemsIndexed(results) { index, result ->
                                    SubjectResultCard(result = result, index = index)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                        }
                    }
                    Button(
                        onClick = {

                            if(!isConnected) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Keine Internetverbindung", "OK", duration = androidx.compose.material3.SnackbarDuration.Short)
                                }
                            } else {
                                navigateToQuiz(subject, focus)

                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF009DE0)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp)

                    ) {
                        Text("Quiz starten", style = MaterialTheme.typography.bodyMedium, color = Color.White )
                    }
                }
            }
        )
    }
}

@Composable
fun SubjectResultCard(result: Result, index: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
            .background(Color(0xFFEAF2FF), RoundedCornerShape(8.dp))
            .height(70.dp)

    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        ) {
            if(index<=2) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = "Result",
                    tint = if(index==0) Color(0xFFFF9913) else if(index==1) Color(0xFFC7C1BA) else if(index==2) Color(0xFF986017) else Color(0xFFC4C4C4),
                    modifier = Modifier.size(35.dp)
                )

            } else {
                Text(text = "#${index+1}", style = MaterialTheme.typography.bodyLarge)
            }

        }

        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text( formatDate(result.resultDateTime!!),  style = MaterialTheme.typography.bodyMedium)
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        ) {
            Box {
                CircularProgressIndicator(
                    progress = { (result.resultScore!! / 100f).toFloat() },
                    trackColor = Color(0xFFF4F3F6),
                    color = Color(0xFF006FFD),
                    strokeWidth = 6.dp
                )

                Text(
                    text = "${result.resultScore?.roundToInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

    }
}

fun formatDate(input: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val dateTime = LocalDateTime.parse(input, inputFormatter)

    return dateTime.format(outputFormatter)
}

@Composable
fun ChallengeBottomSheet(onClose: () -> Unit, challenge: OpenChallenges?, onChallengeDecline: () -> Unit, onChallengeAccept: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xFFF8F9FE), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Text(challenge?.focus?.focusName ?: challenge?.subject?.subjectName ?: "", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFFEAF2FF), shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Icon",
                            tint = Color(0xFFB4DBFF),
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(challenge?.friendship?.friend?.userFullname ?: "", style = MaterialTheme.typography.bodyMedium)
                        Text(challenge?.friendship?.friend?.userClass ?: "", style = MaterialTheme.typography.bodySmall)
                    }

                }

                Box {
                    CircularProgressIndicator(
                        progress = { (challenge?.friendScore?.resultScore?.div(100))?.toFloat() ?: 0f },
                        trackColor = Color(0xFFF4F3F6),
                        color = Color(0xFFFB6E5C),
                        strokeWidth = 8.dp,
                        modifier = Modifier.size(60.dp)
                    )

                    Text(
                        text = "${challenge?.friendScore?.resultScore?.roundToInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Button(
                    onClick = { onChallengeDecline() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround

                    ) {
                        Text("Ablehnen", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFFF3B30)
                        )
                    }

                }

                Button(
                    onClick = { onChallengeAccept() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround

                    ) {
                        Text("Annehmen", style = MaterialTheme.typography.bodyMedium, color = Color.Black )

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            tint = Color(0xFF007AFF)
                        )
                    }

                }



            }

        }
    }
}