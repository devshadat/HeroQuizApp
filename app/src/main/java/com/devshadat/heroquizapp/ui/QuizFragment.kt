package com.devshadat.heroquizapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.devshadat.heroquizapp.R
import com.devshadat.heroquizapp.api.RetrofitService
import com.devshadat.heroquizapp.data.Question
import com.devshadat.heroquizapp.databinding.FragmentQuizBinding
import com.devshadat.heroquizapp.repository.QuizRepository
import com.devshadat.heroquizapp.viewmodel.QuizViewModel
import com.devshadat.heroquizapp.viewmodel.QuizViewModelFactory
import java.util.Locale
import java.util.concurrent.TimeUnit

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private lateinit var quizViewModel: QuizViewModel
    private val binding get() = _binding!!
    private val retrofitService = RetrofitService.getInstance()

    private var questionsList = listOf<Question>()
    private var index: Int = 0
    private lateinit var questionModel: Question

    private var correctAnswerCount: Int = 0
    private var wrongAnswerCount: Int = 0
    private var totalScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        quizViewModel =
            ViewModelProvider(this, QuizViewModelFactory(QuizRepository(retrofitService))).get(
                QuizViewModel::class.java
            )
        quizViewModel.getQuizQuestions()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quizViewModel.setScore(totalScore)
        quizViewModel.getQuizQuestions().observe(viewLifecycleOwner, Observer {
            questionsList = it.toList()
            questionModel = questionsList[index]
            if(questionsList.size>0) {
                binding.progressBar.visibility = View.INVISIBLE
            }
            setQuizQuestion()
            countdown()
            setSelectedAnswer()
        })

    }

    private fun setSelectedAnswer() {

        binding.optionOne.setOnClickListener {
            disableButton()
            checkAnswer("A", binding.optionOne)
        }
        binding.optionTwo.setOnClickListener {
            disableButton()
            checkAnswer("B", binding.optionTwo)
        }
        binding.optionThree.setOnClickListener {
            disableButton()
            checkAnswer("C", binding.optionThree)
        }
        binding.optionFour.setOnClickListener {
            disableButton()
            checkAnswer("D", binding.optionFour)
        }
    }

    private fun checkAnswer(s: String, id: Button) {
        if (questionModel.correctAnswer == s) {
            id.background = resources.getDrawable(R.drawable.right_bg)
            totalScore += questionModel.score
            quizViewModel.setScore(totalScore)
        } else {
            id.background = resources.getDrawable(R.drawable.wrong_bg)

        }
    }

    fun countdown() {
        var duration: Long = TimeUnit.SECONDS.toMillis(5)

        object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                var sDuration: String = String.format(
                    Locale.ENGLISH,
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
                binding.countdown.text = sDuration
            }

            override fun onFinish() {
                index++
                if (index < questionsList.size) {
                    questionModel = questionsList[index]
                    setQuizQuestion()
                    resetBackground()
                    enableButton()
                    countdown()

                } else {
                    finishGame()
                }

            }

        }.start()
    }

    private fun finishGame() {
        saveHighScore()
        val action = QuizFragmentDirections.actionQuizFragmentToMenuFragment()
        findNavController().navigate(action)
    }

    private fun saveHighScore() {

        val sharedPreference =
            context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference?.edit()
        val previousScore = sharedPreference?.getInt("score", 0)
        if (totalScore > previousScore!!) {
            editor?.putInt("score", totalScore)
            editor?.apply()
        }
    }

    private fun resetBackground() {
        binding.optionOne.background = resources.getDrawable(R.drawable.radio_background_default)
        binding.optionTwo.background = resources.getDrawable(R.drawable.radio_background_default)
        binding.optionThree.background = resources.getDrawable(R.drawable.radio_background_default)
        binding.optionFour.background = resources.getDrawable(R.drawable.radio_background_default)
    }

    @SuppressLint("SetTextI18n")
    private fun setQuizQuestion() {
        binding.apply {
            currentQuizQuestion.text = questionModel.question
            optionOne.text = questionModel.answers.A
            optionTwo.text = questionModel.answers.B
            optionThree.text = questionModel.answers.C
            optionFour.text = questionModel.answers.D
            currentQuizPoint.text = "${questionModel.score} Point"
            questionModel.questionImageUrl.let {
                Glide.with(requireContext()).load(it).into(currentQuizImage)
            }
            currentQuestionNo.text = "Question:${index + 1}/${questionsList.size}"
            quizViewModel.getScore().observe(viewLifecycleOwner, Observer {
                totalQuizScore.text = "Score: $it"
            })
        }
    }

    private fun correctAns(option: Button) {
        option.background = resources.getDrawable(R.drawable.right_bg)
        correctAnswerCount++
    }

    private fun wrongAns(option: Button) {
        option.background = resources.getDrawable(R.drawable.wrong_bg)
        wrongAnswerCount++
    }

    private fun disableButton() {
        binding.apply {
            optionOne.isClickable = false
            optionTwo.isClickable = false
            optionThree.isClickable = false
            optionFour.isClickable = false
        }
    }

    private fun enableButton() {
        binding.apply {
            optionOne.isClickable = true
            optionTwo.isClickable = true
            optionThree.isClickable = true
            optionFour.isClickable = true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = QuizFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}