package com.example.quizit_android_app.ui.home

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.quizit_android_app.R
import com.example.quizit_android_app.ui.theme.Typography

data class Subject(
    val subjectName: String,
    val subjectImageAddress: String
)

@Composable
fun HomeScreen() {
    val subjects = remember {
        listOf(
            Subject("Angewandte Mathematik", "https://schoolizer.com/img/articles_photos/17062655360.jpg"),
            Subject("GGP", "https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"),
            Subject("SEW", "https://blog.planview.com/de/wp-content/uploads/2020/01/Top-6-Software-Development-Methodologies.jpg")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_lightmode),
                contentDescription = "QuizIT Logo",
                modifier = Modifier
                    .size(125.dp),
                contentScale = ContentScale.FillBounds

            )

            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp)
            )

        }

        SubjectSection(subjects = subjects)

        Spacer(modifier = Modifier.size(32.dp))

        StatisticsSection()

    }




}


@Composable
fun SubjectSection(subjects: List<Subject>) {


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            "Deine Fächer",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )

        Text(
            text = "mehr anzeigen",
            modifier = Modifier
                .clickable {  }
        )
    }



    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(subjects) { index, subject ->
            SubjectCard(subject = subject)
        }
    }
}

@Composable
fun SubjectCard(subject: Subject) {

    Card(
        modifier = Modifier
            .width(250.dp)
            .padding(end = 16.dp),

        colors = CardDefaults.cardColors(containerColor = Color(0xFFeaf2ff)),

    ) {
        AsyncImage(
            model = subject.subjectImageAddress ,
            contentDescription = "Subject Image",
            modifier = Modifier
                .width(250.dp)
                .height(150.dp)
                .padding(bottom = 8.dp),
            contentScale = ContentScale.FillBounds
        )

        Text(
            text = subject.subjectName,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.size(4.dp))

        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(8.dp),

            border = BorderStroke(1.dp, color = Color(0xFF007aff)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Fortschritt", color = Color(0xFF007aff))
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
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,

        )

        Text(
            text = "mehr anzeigen",
            modifier = Modifier
                .clickable {  }
        )
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}