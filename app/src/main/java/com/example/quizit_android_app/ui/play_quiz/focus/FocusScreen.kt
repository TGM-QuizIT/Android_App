package com.example.quizit_android_app.ui.play_quiz.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.quizit_android_app.models.Focus
import com.example.quizit_android_app.models.Subject
import com.example.quizit_android_app.ui.theme.Typography
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
    navigateToQuiz: (Int) -> Unit,
    navigateBack: () -> Unit,
    focusViewModel: FocusViewModel = hiltViewModel()
) {
    val focusList = focusViewModel.focusList
    val subject = focusViewModel.subject

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
                            text = subject!!.subjectName,
                            style = Typography.titleLarge
                        )
                    }
                }
            }
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {



                LazyColumn {

                    item {
                        FocusCard(
                            type = CardType.Subject,
                            subject = subject,
                            onQuizStart = {
                                navigateToQuiz(it)
                            }
                        )
                    }
                    items(focusList) {
                        FocusCard(
                            type = CardType.Focus,
                            focus = it,
                            onQuizStart = {
                                navigateToQuiz(it)
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun FocusCard(
    type: CardType,
    subject: Subject? = null,
    focus: Focus? = null,
    onQuizStart: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = { }),
        colors = CardDefaults.cardColors(
            containerColor = if (type == CardType.Subject) Color(0xFFEAF2FF) else Color(0xFFF8F9FE),
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            Column(
                Modifier
                    .fillMaxWidth(0.5f)
                    .padding(16.dp)
            ) {
                when (type) {
                    CardType.Subject -> {
                        Text(
                            text = subject?.subjectName ?: "",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "147 Fragen im Pool", // Hier kann eine dynamische Zahl verwendet werden, wenn gewünscht
                            style = Typography.labelLarge
                        )
                    }
                    CardType.Focus -> {
                        Text(
                            text = focus?.focusName ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis

                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${focus?.questionCount ?: 0} Fragen im Pool",
                            style = Typography.labelLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        when (type) {
                            CardType.Subject -> subject?.subjectId?.let { onQuizStart(it) }
                            CardType.Focus -> focus?.focusId?.let { onQuizStart(it) }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                ) {
                    Text("Quiz starten", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }

            }

            Spacer(modifier = Modifier.width(8.dp))

            AsyncImage(
                model = "https://placehold.co/1600x600.png",
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

enum class CardType {
    Subject,
    Focus
}
