package com.example.quizit_android_app.ui.play_quiz.subject

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.quizit_android_app.models.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var _subjectList = mutableStateOf(listOf<Subject>())
    val subjectList: List<Subject> get() = _subjectList.value


    init {
        setSubjects()
    }

    private fun setSubjects() {
        _subjectList.value = listOf(
            Subject(
                subjectId = 1,
                "Angewandte Mathematik",
                "https://schoolizer.com/img/articles_photos/17062655360.jpg"
            ),
            Subject(
                subjectId = 2,
                "GGP",
                "https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"
            ),
            Subject(
                subjectId = 3,
                "SEW",
                "https://blog.planview.com/de/wp-content/uploads/2020/01/Top-6-Software-Development-Methodologies.jpg"
            ),
            Subject(
                subjectId = 4,
                "SEW",
                "https://blog.planview.com/de/wp-content/uploads/2020/01/Top-6-Software-Development-Methodologies.jpg"
            )
        )
    }

}