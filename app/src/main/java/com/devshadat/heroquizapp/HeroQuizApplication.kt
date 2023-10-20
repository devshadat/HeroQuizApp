package com.devshadat.heroquizapp

import QuizDatabase
import android.app.Application

class HeroQuizApplication : Application() {

    val database: QuizDatabase by lazy { QuizDatabase.getDatabase(this) }
}