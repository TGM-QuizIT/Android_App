package com.example.quizit_android_app.model

import android.content.Context
import com.example.quizit_android_app.QuizITApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}
