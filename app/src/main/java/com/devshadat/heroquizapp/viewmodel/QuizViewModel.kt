/*
 * Copyright (C) 2023 devshadat
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devshadat.heroquizapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devshadat.heroquizapp.data.Question
import com.devshadat.heroquizapp.data.Questions
import com.devshadat.heroquizapp.repository.QuizRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private var questionList: MutableLiveData<List<Question>>? = null
    val errorMessage = MutableLiveData<String>()
    private val totalScore = MutableLiveData<Int>()

    fun getQuizQuestions(): LiveData<List<Question>> {

        if (questionList == null) {
            questionList = MutableLiveData<List<Question>>()

            getAllQuestions()
        }

        return questionList as MutableLiveData<List<Question>>
    }

    private fun getAllQuestions() {
        val response = repository.getAllQuestions()
        response.enqueue(object : Callback<Questions> {
            override fun onResponse(call: Call<Questions>, response: Response<Questions>) {
                questionList?.value = response.body()?.questions
            }

            override fun onFailure(call: Call<Questions>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getScore(): MutableLiveData<Int> {
        return totalScore
    }

    fun setScore(score: Int) {
        totalScore.postValue(score)
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class QuizViewModelFactory constructor(private val repository: QuizRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            QuizViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

