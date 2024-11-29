package com.example.quizit_android_app.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.quizit_android_app.R
import com.example.quizit_android_app.models.Subject
import com.example.quizit_android_app.ui.theme.Typography



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSubject: () -> Unit,
    navigateToFocus: (Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()

) {


    val subjectList = homeViewModel.subjectList


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_lightmode),
                        contentDescription = "QuizIT Logo",
                        modifier = Modifier
                            .width(125.dp)
                            .height(42.dp),
                        contentScale = ContentScale.FillBounds

                    )

                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(24.dp)
                    )

                }
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.padding(top = 32.dp)

            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {

                SubjectSection(subjects = subjectList, onClick = {
                    navigateToSubject()
                }, navigateToFocus = {
                    navigateToFocus(it)
                })

                Spacer(modifier = Modifier.size(32.dp))

                StatisticsSection()
            }
        },
        contentWindowInsets = WindowInsets(0.dp),
    )


}

@Composable
fun SubjectSection(subjects: List<Subject>, onClick: () -> Unit, navigateToFocus: (Int) -> Unit) {


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            //.padding(top = 8.dp)
    ) {
        Text(
            "Deine Fächer",
            style = Typography.titleMedium
        )

        Text(
            text = "mehr anzeigen",
            modifier = Modifier
                .clickable { onClick() },
            style = Typography.titleSmall
        )
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(subjects) { index, subject ->
            SubjectCard(subject = subject, 250.dp, navigateToFocus = navigateToFocus)
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}



@Composable
fun SubjectCard(subject: Subject, width: Dp, navigateToFocus: (Int) -> Unit) {

    Card(
        modifier = Modifier
            .then(
                if (width == 0.dp) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.width(width)
                }
            ),

        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),

    ) {
        AsyncImage(
            model = subject.subjectImageAddress ,
            contentDescription = "Subject Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 8.dp),
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = subject.subjectName,
            style = Typography.bodyMedium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.size(4.dp))

        Button(
            onClick = { navigateToFocus(subject.subjectId) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(8.dp),

            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.tertiary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Schwerpunkte", style= Typography.bodyMedium, color = MaterialTheme.colorScheme.tertiary)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun StatisticsSection() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            "Deine Statistiken",
            style = Typography.titleMedium

        )

        Text(
            text = "mehr anzeigen",
            modifier = Modifier
                .clickable {  },
            style = Typography.titleSmall
        )
    }
}

