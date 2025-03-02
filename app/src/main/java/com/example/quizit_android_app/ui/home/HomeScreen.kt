package com.example.quizit_android_app.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.quizit_android_app.R
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.model.retrofit.UserStatsResponse
import com.example.quizit_android_app.ui.social.StatisticsCard
import com.example.quizit_android_app.ui.theme.Typography
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.quizit_android_app.model.retrofit.Challenge
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.ui.play_quiz.quiz.ChallengeBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navigateToSubject: () -> Unit,
    navigateToFocus: (Subject) -> Unit,
    navigateToChallenge: () -> Unit,
    navigateToPlayChallenge: (OpenChallenges?) -> Unit,
    navigateToStatistics: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val subjectList = homeViewModel.subjectList
    val stats = homeViewModel.stats
    val isLoading = homeViewModel.isLoading
    val challenges = homeViewModel.challenges

    var isRefreshing by remember { mutableStateOf(false) }

    // Pull to Refresh State
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            homeViewModel.refreshData {
                isRefreshing = false
            }
        }
    )

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
                    onChallengeAccept = {
                        coroutineScope.launch {
                            navigateToPlayChallenge(challenge)
                            challengeSheetState.hide()
                        }
                    },
                    onChallengeDecline = {
                        coroutineScope.launch {
                            challengeSheetState.hide()
                        }
                    }
                )
            } ?: Box(modifier = Modifier.size(1.dp))
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_lightmode),
                        contentDescription = "QuizIT Logo",
                        modifier = Modifier
                            .width(125.dp)
                            .aspectRatio(975f / 337f),
                        contentScale = ContentScale.FillBounds
                    )
                }
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)
                        .padding(paddingValues)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            trackColor = Color.Gray
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp)
                                .padding(start = 16.dp),
                        ) {
                            item {
                                SubjectSection(
                                    subjects = subjectList,
                                    navigateToSubjects = { navigateToSubject() },
                                    navigateToFocus = { subject -> navigateToFocus(subject) }
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                            }
                            item {
                                ChallengeSection(
                                    navigateToChallenge = { navigateToChallenge() },
                                    challenges = challenges,
                                    onChallengeCardClick = { challenge ->
                                        selectedChallenge = challenge
                                        coroutineScope.launch { challengeSheetState.show() }
                                    }
                                )
                                Spacer(modifier = Modifier.size(32.dp))
                            }
                            item {
                                StatisticsSection(
                                    navigateToStatistics = { navigateToStatistics() },
                                    stats = stats
                                )
                                Spacer(modifier = Modifier.size(16.dp))
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


}






@Composable
fun SubjectSection(subjects: List<Subject>, navigateToSubjects: () -> Unit, navigateToFocus: (Subject) -> Unit) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        ) {
            Text(
                "Deine Fächer",
                style = Typography.titleMedium
            )

            Text(
                text = "mehr anzeigen",
                modifier = Modifier
                    .clickable { navigateToSubjects() },
                style = Typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        if(subjects.isEmpty()) {
            NoContentPlaceholder(id = R.drawable.no_subject_placeholder)
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                itemsIndexed(subjects) { index, subject ->
                    SubjectCard(subject = subject, 250.dp, navigateToFocus = { subject ->
                        navigateToFocus(subject)
                    })
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }

        }


    }

}



@Composable
fun SubjectCard(subject: Subject, width: Dp, navigateToFocus: (Subject) -> Unit) {

    Card(
        modifier = Modifier
            .then(
                if (width == 0.dp) {
                    Modifier.fillMaxWidth().clickable { navigateToFocus(subject) }
                } else {
                    Modifier.width(width).clickable { navigateToFocus(subject) }
                }
            ),

        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF2FF)),

    ) {
        AsyncImage(
            model = subject.subjectImageAddress,
            contentDescription = "Subject Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp, top = 8.dp)
                .fillMaxWidth()
                .aspectRatio(2f / 1f),


        )


        Column(
            modifier = Modifier.background(color = Color(0xFFF8F9FE))
        ) {
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = subject.subjectName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Button(
                onClick = {
                    navigateToFocus(subject)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 12.dp),

                border = BorderStroke(1.5.dp, color = Color(0xFF006FFD)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Schwerpunkte", style= Typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color(0xFF006FFD))
            }


        }


    }
}

@Composable
fun ChallengeSection(
    navigateToChallenge: () -> Unit,
    onChallengeCardClick: (OpenChallenges) -> Unit,
    challenges: List<OpenChallenges>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        ) {
            Text(
                "Deine Herausforderungen",
                style = Typography.titleMedium
            )

            Text(
                text = "mehr anzeigen",
                modifier = Modifier
                    .clickable { navigateToChallenge() },
                style = Typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        if(challenges.isEmpty()) {
            NoContentPlaceholder(id = R.drawable.no_open_challenges_placeholder)
        } else {
            LazyRow {
                items(challenges) { challenge ->

                    if(challenge.friendScore?.resultScore != null) {
                        OpenChallengeCard(ChallengeType.OVERALL, challenge, onClick = { onChallengeCardClick(challenge)  }, )
                        Spacer(modifier = Modifier.width(16.dp))

                    }

                }
            }
        }




    }


}

@Composable
fun OpenChallengeCard(type: ChallengeType, challenge: OpenChallenges, onClick : () -> Unit) {


    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() },
        colors =  when (type) {
            ChallengeType.FRIEND -> { CardDefaults.cardColors(containerColor = if(challenge.focus == null) Color(0xFFEAF2FF) else Color(0xFFf8f9fe))}
            else -> { CardDefaults.cardColors(containerColor = Color(0xFFEAF2FF)) }
        }


    ) {
        Column(
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp, bottom = 8.dp)
            ) {

                when (type) {
                    ChallengeType.FRIEND -> {
                        AsyncImage(
                            model = challenge.focus?.focusImageAddress ?: challenge.subject?.subjectImageAddress,
                            contentDescription = "Challenge Image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .aspectRatio(2f/1f)

                        )

                    }
                    else -> {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .background(Color(0xFFEAF2FF), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "User Icon",
                                    tint = Color(0xFFB4DBFF),
                                    modifier = Modifier.size(35.dp)
                                )
                            }



                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = challenge.friendship?.friend?.userFullname!!,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                                Text(
                                    text = challenge.friendship?.friend?.userClass!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black
                                )
                            }
                        }

                    }

                }

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F9FE))
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = when(type) {
                            ChallengeType.SUBJECT -> challenge.focus?.focusName ?: challenge.subject?.subjectName!!
                            else -> challenge.focus?.focusName ?: challenge.subject?.subjectName!!
                        },
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF006FFD), shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(
                                    challenge.friendScore?.resultScore
                                        ?.toFloat()
                                        ?.div(100) ?: 0f
                                )
                                .height(30.dp)
                                .background(Color(0xFF006FFD), shape = RoundedCornerShape(8.dp))
                        )

                        Text(
                            text = "${challenge.friendScore?.resultScore}%",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.labelLarge
                        )

                    }
                }
            }
        }
    }
}

enum class ChallengeType {
    OVERALL,
    FRIEND,
    SUBJECT
}
@Composable
fun StatisticsSection(navigateToStatistics: () -> Unit, stats: UserStatsResponse?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    ) {

        if(stats!=null) {


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()


            ) {

                Text(
                    "Statistiken",
                    style = Typography.titleMedium

                )

                Text(
                    text = "mehr anzeigen",
                    modifier = Modifier
                        .clickable { navigateToStatistics() },
                    style = Typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.size(16.dp))


            StatisticsCard(onClick = { navigateToStatistics() }, stats = stats)
        }


    }
}

@Composable
fun NoContentPlaceholder(id: Int) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "No Content Placeholder",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4800f/2000f)
    )

}

