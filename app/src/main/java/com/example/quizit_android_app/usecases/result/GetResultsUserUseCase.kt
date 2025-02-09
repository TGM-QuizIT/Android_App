package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Result
import javax.inject.Inject

class GetResultsUserUseCase @Inject constructor(
    val dataRepo: DataRepo
){
    suspend operator fun invoke(amount: Int? = null) : List<Result> {
        return dataRepo.fetchResultsOfUser(amount)
    }
}