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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizit_android_app.ui.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizScreen(
    navigateBack: () -> Unit,
    quizViewModel: QuizViewModel = hiltViewModel()
) {

    val currentQuestionIndex = quizViewModel.currentQuestionIndex.value
    val selectedAnswers = quizViewModel.selectedAnswers.value
    val questions = quizViewModel.questions.value
    val focus = quizViewModel.focus.value


    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            if (currentQuestionIndex < questions.size) {
                QuizTopBar(currentQuestionIndex + 1, questions.size, focus, onClick = {
                    navigateBack()
                })
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
                    modifier = Modifier.padding(paddingValues)
                )
            } else {


                val userScore = quizViewModel.calculateScore()
                val userResults = quizViewModel.getResult()
                QuizResult(focus = focus,score = userScore, results = userResults, onCloseResult = { navigateBack()  } )
            }
        },
        modifier = Modifier.padding(top = 16.dp)
    )



}

@Composable
fun QuizTopBar(currentQuestion: Int, totalQuestions: Int, focus: String, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
                onClick = { onClick() },
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
fun QuizResult(focus: String,score: Float, results: List<ResultItem>, onCloseResult: () -> Unit) {

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.size(32.dp))

                    Box(

                    ) {
                        Text(
                            text = focus,
                            style = Typography.titleMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Button(
                        onClick = {
                            onCloseResult()
                        },
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
            }
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(paddingValues),
            ) {
                val correctAnswers = results.count { it.isCorrect }
                val incorrectAnswers = results.count { !it.isCorrect }
                val percentage = (score * 100).toInt()

                Text(
                    modifier = Modifier.padding(start=16.dp),
                    text = "Dein Resultat",
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.size(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.size(40.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                        //.padding(8.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { score },
                            modifier = Modifier.fillMaxSize(),
                            color = Color(0xFF006FFD),
                            strokeWidth = 13.dp,
                            trackColor = Color.LightGray,

                            )
                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
                    }


                    Spacer(modifier = Modifier.size(32.dp))

                    // Buttons
                    Column {
                        Button(
                            onClick = { /* Freund herausfordern */ },
                            colors = ButtonDefaults.buttonColors(
                                MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),

                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Freund herausfordern", color = Color.Black, style= MaterialTheme.typography.titleSmall)
                        }

                        Spacer(modifier = Modifier.size(8.dp))


                        Button(
                            onClick = { /* Historie anzeigen */ },
                            colors = ButtonDefaults.buttonColors(
                                MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),

                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Historie", color = Color.Black, style = MaterialTheme.typography.titleSmall)
                        }
                    }

                }

                Spacer(modifier = Modifier.size(16.dp))

                LazyColumn {
                    itemsIndexed(results) { index,result ->
                        ResultCard(result = result, questionIndex=index)
                    }
                }



            }


        }
    )

}

@Composable
fun ResultCard(result: ResultItem, questionIndex: Int) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.5.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = result.question.questionText,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "#${questionIndex + 1}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,

                )

        }
                    


        Spacer(modifier = Modifier.height(8.dp))

        result.question.options.forEach { option ->
            var backgroundColor = Color.White
            var icon: ImageVector? = null

            // Bedingungen prüfen und Werte entsprechend setzen
            if (option.optionId in result.userAnswer && option.optionCorrect) {
                backgroundColor = Color(0xFF6FFD89)
                icon = Icons.Default.Check

            } else if (option.optionId in result.userAnswer && !option.optionCorrect) {
                backgroundColor = Color(0xFFFB6E5C)
                icon = Icons.Default.Close

            } else if (option.optionId !in result.userAnswer && option.optionCorrect) {
                backgroundColor = Color(0xFFE1E1E1)
                icon = Icons.Default.Check

            }

            // Option anzeigen
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                // Text der Option
                Text(
                    text = option.optionText,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )

                if (icon != null) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }

}