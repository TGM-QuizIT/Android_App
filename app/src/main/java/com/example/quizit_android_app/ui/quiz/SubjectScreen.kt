package com.example.quizit_android_app.ui.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.quizit_android_app.ui.home.Subject
import com.example.quizit_android_app.ui.home.SubjectCard
import com.example.quizit_android_app.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen() {
    val subjects = remember {
        listOf(
            Subject("Angewandte Mathematik", "https://schoolizer.com/img/articles_photos/17062655360.jpg"),
            Subject("GGP", "https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"),
            Subject("SEW", "https://blog.planview.com/de/wp-content/uploads/2020/01/Top-6-Software-Development-Methodologies.jpg") ,
            Subject("SEW", "https://blog.planview.com/de/wp-content/uploads/2020/01/Top-6-Software-Development-Methodologies.jpg")
        )
    }

    val scrollState = rememberScrollState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
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
                scrollBehavior = scrollBehavior,
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Fach auswählen", style = Typography.titleLarge)
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
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            ) {
                item {
                    Text(
                        text = "Fach auswählen",
                        style = Typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(subjects) { subject ->
                    SubjectCard(subject = subject, width = 0.dp)

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    )
}