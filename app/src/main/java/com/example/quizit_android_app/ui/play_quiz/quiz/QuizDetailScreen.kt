package com.example.quizit_android_app.ui.play_quiz.quiz


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizit_android_app.ui.social.Result

@Composable
fun QuizDetailScreen(
    viewModel: QuizDetailViewModel = hiltViewModel(),
) {
    val results = viewModel.results
    val challenges = viewModel.challenges



}

@Composable
fun QuizHistorySection(results: List<Result>) {

    Text("Deine Resultate", style = MaterialTheme.typography.titleMedium)

    LazyColumn {
        itemsIndexed(results) { index, result ->
            ResultListItem(result = result, number = index + 1)
        }
    }


}

@Composable
fun ResultListItem(result: Result, number: Int) {

}

@Composable
fun ChallengeSection(challenges: List<Challenge>) {

}