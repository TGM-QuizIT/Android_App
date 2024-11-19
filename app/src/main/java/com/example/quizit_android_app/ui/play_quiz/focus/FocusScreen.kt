package com.example.quizit_android_app.ui.play_quiz.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.quizit_android_app.ui.home.Subject
import com.example.quizit_android_app.ui.theme.Typography
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
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
                                text = "GGP",
                                style = Typography.titleLarge
                            )
                        }
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
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
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {

                val subject = Subject("GGP", "https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg")

                SubjectCard(subject = subject )

                val focusList = listOf(
                    Focus("2. Weltkrieg", 20,"https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"),
                    Focus("Mittelalter", 24,"https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"  ),
                    Focus("Zwischenkriegszeit", 30,"https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"  ),
                    Focus("Ideologien", 32,"https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"  ),
                    Focus("Kalter Krieg", 41,"https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"  )
                )

                LazyColumn {
                    items(focusList) {
                        FocusCard(focus = it)
                    }
                }
            }
        }
    )
}

@Composable
fun SubjectCard(subject: Subject) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = { }),
        //elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F3F6)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = subject.subjectImageAddress,
                contentDescription = "Focus Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.subjectName,
                    style = Typography.titleMedium
                )
                Text(
                    text = "147 Fragen insgesamt im Pool",
                    style = Typography.labelLarge
                )
            }



            IconButton(
                onClick = {},
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun FocusCard(focus: Focus) {

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = { }),
        //elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F3F6)
        )

    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = focus.imageAddress,
                contentDescription = "Focus Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(8.dp))


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = focus.title,
                    style = Typography.titleMedium
                )
                Text(
                    text = "${focus.questionCount} Fragen im Pool",
                    style = Typography.labelLarge
                )
            }



            IconButton(
                onClick = {},
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
    }
}

data class Focus(
    val title: String,
    val questionCount: Int,
    val imageAddress: String
)

