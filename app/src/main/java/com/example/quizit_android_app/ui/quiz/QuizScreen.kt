package com.example.quizit_android_app.ui.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quizit_android_app.ui.home.Subject
import com.example.quizit_android_app.ui.home.SubjectCard
import com.example.quizit_android_app.ui.theme.Typography

@Composable
fun QuizScreen() {
    val subjects = remember {
        listOf(
            Subject("Angewandte Mathematik", "https://schoolizer.com/img/articles_photos/17062655360.jpg"),
            Subject("GGP", "https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"),
            Subject("SEW", "https://blog.planview.com/de/wp-content/uploads/2020/01/Top-6-Software-Development-Methodologies.jpg") ,
            Subject("SEW", "https://blog.planview.com/de/wp-content/uploads/2020/01/Top-6-Software-Development-Methodologies.jpg")
        )
    }

    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Fach auswählen",
            style = Typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))
        subjects.forEach {
            SubjectCard(subject = it, width = 0.dp)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}