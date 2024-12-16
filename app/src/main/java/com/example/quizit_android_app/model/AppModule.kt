package com.example.quizit_android_app.model

import android.content.Context
import com.example.quizit_android_app.QuizITApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/*
TODO: hilt error
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: QuizITApplication): Context {
        return application.applicationContext
    }
}*/
