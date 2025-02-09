package com.example.quizit_android_app.usecases.localdata.friendship

import com.example.quizit_android_app.model.ContentDataStore
import javax.inject.Inject

class GetLocalPendingFriendsUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() = contentDataStore.getPendingFriends()
}