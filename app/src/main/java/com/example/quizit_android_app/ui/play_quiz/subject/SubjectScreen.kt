package com.example.quizit_android_app.ui.play_quiz.subject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizit_android_app.ui.home.SubjectCard
import com.example.quizit_android_app.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    navigateToFocus: (Int) -> Unit,
    navigateBack: () -> Unit,
    subjectViewModel: SubjectViewModel = hiltViewModel()
) {


    val subjectList = subjectViewModel.subjectList


    val scrollState = rememberScrollState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
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

                items(subjectList) { subject ->
                    SubjectCard(subject = subject, width = 0.dp, navigateToFocus = {
                        navigateToFocus(it)
                    })

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    )
}