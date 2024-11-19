package com.example.quizit_android_app.ui.quiz

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizit_android_app.ui.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizScreen() {
    val quizViewModel: QuizViewModel = viewModel()

    val currentQuestionIndex = quizViewModel.currentQuestionIndex.value
    val selectedAnswers = quizViewModel.selectedAnswers.value
    val questions = quizViewModel.questions.value
    val focus = quizViewModel.focus.value
    val showSelectionError = quizViewModel.showSelectionError.value

    Scaffold(
        topBar = {
            if (currentQuestionIndex < questions.size) {
                QuizTopBar(currentQuestionIndex + 1, questions.size, focus)
            }
        },
        content = { paddingValues ->
            if (currentQuestionIndex < questions.size) {
                QuizQuestion(
                    question = questions[currentQuestionIndex],
                    selectedAnswers = selectedAnswers,
                    onSelected = { optionId ->
                        quizViewModel.toggleAnswer(optionId)
                    },
                    onNext = { quizViewModel.nextQuestion() },
                    showError = showSelectionError,
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                val score = quizViewModel.calculateScore()
                //QuizResult(score = score, totalQuestions = questions.size, onRestart = { quizViewModel.nextQuestion()  } )
            }
        }
    )



}

@Composable
fun QuizTopBar(currentQuestion: Int, totalQuestions: Int, focus: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "$currentQuestion/$totalQuestions",
                style = Typography.bodyMedium,
                color = Color.Gray
            )

            Box(
                //modifier = Modifier
                    //.fillMaxWidth()
            ) {
                Text(
                    text = focus,
                    style = Typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Button(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",

                    modifier = Modifier
                        .size(16.dp),
                    tint = Color.Black
                )

            }


        }

        Spacer(modifier = Modifier.size(8.dp))

        LinearProgressIndicator(
            progress = { (currentQuestion.toFloat() / totalQuestions.toFloat()).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.tertiary.copy()
        )
    }
}


@Composable
fun QuizQuestion(
    question: Question,
    selectedAnswers: List<Int>,
    onSelected: (Int) -> Unit,
    onNext: () -> Unit,
    showError: Boolean,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp * 0.2f).dp),

            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )


        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = question.questionText,
                    style = Typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

        }


        Column(modifier = Modifier.fillMaxWidth()) {
            question.options.forEach { option ->
                OptionItem(
                    option = option,
                    isSelected = selectedAnswers.contains(option.optionId),
                    onSelected = onSelected
                )

            }
        }


        Column(
            modifier = Modifier
                .align(Alignment.End)
        ) {
            if (showError) {
                Text(
                    text = "Bitte wählen Sie mindestens eine Antwort aus.",
                    color = MaterialTheme.colorScheme.error,
                    style = Typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(32.dp))
            }

            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
                    .padding(8.dp),

                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.tertiary),
                //enabled = !showError,
                shape = RoundedCornerShape(8.dp),

                ) {
                Text(text = "Weiter", style= Typography.bodyMedium, color = MaterialTheme.colorScheme.tertiary, )
            }
        }



    }
}

@Composable
fun OptionItem(
    option: Option,
    isSelected: Boolean,
    onSelected: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)

            .clickable { onSelected(option.optionId) }
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (!isSelected) 0.5.dp else 0.dp,
                color = if (!isSelected) Color.Gray else Color.Transparent,
                shape = RoundedCornerShape(8.dp) // Ensure rounded corners for border as well
            )
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) ,


    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = option.optionText,
                style = Typography.bodySmall,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp)
                )
            }
        }


    }
}

@Composable
fun QuizResult(score: Int, totalQuestions: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Glückwunsch!",
            style = Typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Du hast $score von $totalQuestions Fragen richtig beantwortet.",
            style = Typography.bodyLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Nochmal versuchen",
                style = Typography.bodyMedium,
                color = Color.White
            )
        }
    }
}