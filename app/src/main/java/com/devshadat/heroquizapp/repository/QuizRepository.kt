package com.devshadat.heroquizapp.repository


import com.devshadat.heroquizapp.api.RetrofitService

class QuizRepository constructor(private val retrofitService: RetrofitService) {

    fun getAllQuestions() = retrofitService.getAllQuestions()

}