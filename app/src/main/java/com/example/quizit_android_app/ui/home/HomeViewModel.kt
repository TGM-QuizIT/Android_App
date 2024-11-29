package com.example.quizit_android_app.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.quizit_android_app.models.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _subjectList by mutableStateOf(listOf<Subject>())
    val subjectList: List<Subject> get() = _subjectList

    init {
        setSubjects()
    }

    private fun setSubjects() {
        _subjectList = listOf(
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
                subjectId = 3,
                "SEW",
                "https://blog.planview.com/de/wp-content/uploads/2020/01/Top-6-Software-Development-Methodologies.jpg"
            )
        )
    }
}