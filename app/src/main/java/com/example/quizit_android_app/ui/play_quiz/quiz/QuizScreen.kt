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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizit_android_app.model.retrofit.AcceptedFriendship
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.Options
import com.example.quizit_android_app.model.retrofit.Questions
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.ui.social.FriendshipCard
import com.example.quizit_android_app.ui.social.UserCard
import com.example.quizit_android_app.ui.theme.Typography
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizScreen(
    navigateBack: () -> Unit,
    quizViewModel: QuizViewModel = hiltViewModel(),
    navigateToQuizDetail: (Subject, Focus?)  -> Unit
) {

    val currentQuestionIndex = quizViewModel.currentQuestionIndex.value
    val selectedAnswers = quizViewModel.selectedAnswers.value
    val questions = quizViewModel.questions.value
    val isLoading = quizViewModel.isLoading.value

    val focus = quizViewModel.focus.value
    val subject = quizViewModel.subject.value

    val friendships = quizViewModel.friendships.value
    val isModalSheetLoading = quizViewModel.isBottomSheetLoading.value


    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            if (currentQuestionIndex < questions.size) {
                QuizTopBar(currentQuestionIndex + 1, questions.size, focus?.focusName, subject?.subjectName, onClick = {
                    navigateBack()
                })
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            trackColor = Color.Gray
                        )
                    }
                    currentQuestionIndex < questions.size -> {
                        QuizQuestion(
                            question = questions[currentQuestionIndex],
                            selectedAnswers = selectedAnswers,
                            onSelected = { optionId ->
                                quizViewModel.toggleAnswer(optionId)
                            },
                            onNext = { quizViewModel.nextQuestion() },
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                    else -> {
                        val userScore = quizViewModel.calculateScore()
                        val userResults = quizViewModel.getResult()
                        QuizResult(
                            focus = focus?.focusName,
                            subject = subject?.subjectName,
                            score = userScore,
                            results = userResults,
                            onCloseResult = { navigateBack() },
                            navigateToQuizDetail = {
                                navigateToQuizDetail(subject!!, focus)
                            },
                            onFriendClick = { quizViewModel.challengeFriend(it, focus = focus, subject = subject!!) },
                            friendships = friendships,
                            isModalSheetLoading = isModalSheetLoading,
                            onChallengeClicked = { quizViewModel.getFriendships() }
                        )
                    }
                }
            }
        },
        modifier = Modifier.padding(top = 16.dp)
    )



}

@Composable
fun QuizTopBar(currentQuestion: Int, totalQuestions: Int, focus: String?, subject: String?, onClick: () -> Unit) {

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

            Box(
                modifier = Modifier
                    .size(32.dp)

            ) {
                Text(
                    text = "$currentQuestion/$totalQuestions",
                    style = Typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = focus ?: subject ?: "",
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
    question: Questions,
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
                    text = question.questionText!!,
                    style = Typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

        }


        Column(modifier = Modifier.fillMaxWidth()) {

            LazyColumn {
                items(question.options) {option ->
                    OptionItem(
                        option = option,
                        isSelected = selectedAnswers.contains(option.optionId),
                        onSelected = onSelected
                    )

                }
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
    option: Options?,
    isSelected: Boolean,
    onSelected: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)

            .clickable { onSelected(option?.optionId!!) }
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (!isSelected) 0.5.dp else 0.dp,
                color = if (!isSelected) Color.Gray else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
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
                text = option?.optionText!!,
                style = Typography.bodySmall,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(16.dp)
                )
            }
        }


    }
}

@Composable
fun FriendListBottomSheet(
    onHide: () -> Unit,
    isModalSheetLoading: Boolean,
    onFriendClick: (Int) -> Unit,
    friendships: List<AcceptedFriendship>
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()

        ) {

            Text("Freund herausfordern", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.Center))
            IconButton(
                onClick = {onHide()},
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterEnd)

            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black,
                )
            }

        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if(isModalSheetLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    trackColor = Color.Gray
                )
            } else {

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))


                LazyColumn {
                    items(friendships) {friendship ->
                        FriendshipCard(friendship = friendship, navigateToUserDetail = { friendshipId, user ->
                            onFriendClick(friendshipId!!)
                        })
                    }
                }

            }
        }
    }
}


@Composable
fun QuizResult(
    focus: String?,
    subject: String?,
    score: Float, results: List<ResultItem>,
    onCloseResult: () -> Unit,
    navigateToQuizDetail: () -> Unit,
    onFriendClick: (Int) -> Unit,
    isModalSheetLoading: Boolean,
    friendships: List<AcceptedFriendship>,
    onChallengeClicked: () -> Unit,
) {


    val sheetState = androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState  = sheetState,
        sheetContent = {

            FriendListBottomSheet(
                onHide = { coroutineScope.launch { sheetState.hide() } },
                isModalSheetLoading = isModalSheetLoading,
                onFriendClick = { onFriendClick(it) },
                friendships = friendships
            )


        }
    ) {

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
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {


                        Box {
                            if(subject != null && focus != null) {


                                Text(
                                    text = subject,
                                    style = Typography.titleMedium,
                                    modifier = Modifier.align(Alignment.Center)
                                )



                            }
                            else {
                                Spacer(modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(32.dp))
                            }

                        }

                        Box(

                        ) {
                            Text(
                                text = focus ?: subject ?: "",
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
                        modifier = Modifier.padding(start=8.dp),
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
                                trackColor = Color(0xFFF4F3F6)

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
                                onClick = {
                                    coroutineScope.launch {
                                        sheetState.show()
                                        onChallengeClicked()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),

                                shape = RoundedCornerShape(8.dp)
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonAdd,
                                        contentDescription = "Freund herausfordern",
                                        tint = Color.Black,


                                        )
                                    Text(text = "Herausfordern", color = Color.Black, style= MaterialTheme.typography.titleSmall)

                                    Spacer(modifier = Modifier)

                                }

                            }

                            Spacer(modifier = Modifier.size(8.dp))


                            Button(
                                onClick = { navigateToQuizDetail() },
                                colors = ButtonDefaults.buttonColors(
                                    MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),

                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = "Historie ansehen",
                                        tint = Color.Black
                                    )
                                    Text(text = "Historie", color = Color.Black, style = MaterialTheme.typography.titleSmall)

                                    Spacer(modifier = Modifier)

                                }

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
                text = result?.question?.questionText!!,
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
            if (option.optionId in result.userAnswer && option?.optionCorrect!!) {
                backgroundColor = Color(0xFF6FFD89)
                icon = Icons.Default.Check

            } else if (option.optionId in result.userAnswer && !option?.optionCorrect!!) {
                backgroundColor = Color(0xFFFB6E5C)
                icon = Icons.Default.Close

            } else if (option.optionId !in result.userAnswer && option?.optionCorrect!!) {
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
                    text = option?.optionText!!,
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