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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.quizit_android_app.ui.social.StatisticsPopUp
import com.example.quizit_android_app.ui.theme.Typography



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSubject: () -> Unit,
    navigateToFocus: (Subject) -> Unit,
    navigateToChallenge: () -> Unit,
    navigateToStatistics: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()

) {


    val subjectList = homeViewModel.subjectList
    val stats = homeViewModel.stats
    val isLoading = homeViewModel.isLoading


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
            if(isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        trackColor = Color.Gray
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                ) {

                    SubjectSection(subjects = subjectList, navigateToSubjects = {
                        navigateToSubject()
                    }, navigateToFocus = { subject ->
                        navigateToFocus(subject)
                    })

                    Spacer(modifier = Modifier.size(16.dp))

                    ChallengeSection(navigateToChallenge = {
                        navigateToChallenge()
                    })

                    Spacer(modifier = Modifier.size(16.dp))

                    StatisticsSection(
                        navigateToStatistics = {
                            navigateToStatistics()
                        },
                        stats = stats
                    )
                }

            }


        },
    )


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



@Composable
fun SubjectCard(subject: Subject, width: Dp, navigateToFocus: (Subject) -> Unit) {

    Card(
        modifier = Modifier
            .then(
                if (width == 0.dp) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.width(width)
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
    navigateToChallenge: () -> Unit
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


    }


}

@Composable
fun ChallengeCard(type: ChallengeType, challenge: OpenChallenges) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(0.3f),
        colors = CardDefaults.cardColors(containerColor = if(challenge.focus == null) Color(0xFFeaf2ff) else Color(0xFFf8f9fe))
    ) {
        Column {
            when (type) {
                ChallengeType.FRIEND -> {
                    AsyncImage(
                        model = challenge.focus?.focusImageAddress ?: challenge.subject?.subjectImageAddress,
                        contentDescription = "Challenge Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f/1f)
                    )

                }
                else -> {

                    Row(
                        modifier = Modifier.fillMaxWidth()
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

                        Column {
                            Text(
                                text = challenge.friendship?.friend?.userFullname!!,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = challenge.friendship?.friend?.userClass!!,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                }

            }


            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = when(type) {
                    ChallengeType.SUBJECT -> challenge.focus?.focusName ?: challenge.subject?.subjectName!!
                    else -> challenge.focus?.focusName ?: challenge.subject?.subjectName!!
                },
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.size(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF006FFD), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(challenge.friendScore!!.toFloat() / 100)
                        .height(24.dp)
                        .background(Color(0xFF006FFD), shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "${challenge.friendScore}%",
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.labelLarge
                    )

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
        var showPopup: Boolean by remember { mutableStateOf(false) }

        if(showPopup) {
            StatisticsPopUp(onClose = { showPopup = false })
        }

        StatisticsCard(onClick = { showPopup = true }, stats = stats)
    }
}

