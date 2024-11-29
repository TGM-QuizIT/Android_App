package com.example.quizit_android_app.ui.play_quiz.focus

import android.util.Log
import  androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.quizit_android_app.models.Focus
import com.example.quizit_android_app.models.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var _focusList by mutableStateOf(listOf<Focus>())
    val focusList: List<Focus> get() = _focusList

    private var _subject = mutableStateOf<Subject?>(null)
    val subject: Subject? get() = _subject.value

    init {
        val subjectId: Int = savedStateHandle.get<String>("subjectId")?.toIntOrNull() ?: 0
        setFocusList(subjectId)
        setSubject(subjectId)
    }

    private fun setFocusList(id: Int) {

        Log.i("",""+id)
        _focusList = listOf(
            Focus(focusId = 1, "2. Weltkrieg", 20, "https://example.com/image1.jpg"),
            Focus(focusId = 2, "Mittelalter", 24, "https://example.com/image2.jpg"),
            Focus(focusId = 3,"Zwischenkriegszeit", 30, "https://example.com/image3.jpg"),
            Focus(focusId = 4,"Ideologien", 32, "https://example.com/image4.jpg"),
            Focus(focusId = 5,"Kalter Krieg", 41, "https://example.com/image5.jpg")
        )
    }

    private fun setSubject(id: Int) {
        Log.i("",""+id)
        Log.i("",""+id)
        _subject.value = Subject(
            subjectId = 2,
            "GGP",
            "https://thumbs.dreamstime.com/b/stellen-sie-von-den-geografiesymbolen-ein-ausr%C3%BCstungen-f%C3%BCr-netzfahnen-weinleseentwurfsskizze-kritzeln-art-ausbildung-136641038.jpg"
        )

    }


}





