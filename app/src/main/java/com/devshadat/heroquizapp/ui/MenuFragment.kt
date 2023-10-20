/*
 * Copyright (C) 2023 devshadat
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devshadat.heroquizapp.ui

import Quiz
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.devshadat.heroquizapp.HeroQuizApplication
import com.devshadat.heroquizapp.databinding.FragmentMenuBinding
import com.devshadat.heroquizapp.viewmodel.QuizViewModel
import com.devshadat.heroquizapp.viewmodel.QuizViewModelFactory

/**
 * Fragment to add or update an note in the database.
 */
class MenuFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(
            (activity?.application as HeroQuizApplication).database.quizDao()
        )
    }

    lateinit var item: Quiz

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Returns true if the EditTexts are not empty
     *//*   private fun isEntryValid(): Boolean {
           return viewModel.isEntryValid(
               binding.noteTitle.text.toString(),
               binding.noteDetail.text.toString(),
           )
       }
   */
    /**
     * Binds views with the passed in [item] information.
     */
    private fun bind(item: Quiz) {
        binding.apply {
            btnStart.setOnClickListener {
                val action = MenuFragmentDirections.actionMenuFragmentToQuizFragment()
                findNavController().navigate(action)
            }
//            saveAction.setOnClickListener { updateItem() }
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     *//*    private fun addNewItem() {
            if (isEntryValid()) {
                viewModel.addNewNote(
                    binding.noteTitle.text.toString(),
                    binding.noteDetail.text.toString()
                )
                val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteListFragment()
                findNavController().navigate(action)
            }
        }*/

    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     *//*
        private fun updateItem() {
            if (isEntryValid()) {
                viewModel.updateNote(
                    this.navigationArgs.noteId,
                    this.binding.noteTitle.text.toString(),
                    this.binding.noteDetail.text.toString()
                )
                val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteListFragment()
                findNavController().navigate(action)
            }
        }
    */

    /**
     * Called when the view is created.
     * The itemId Navigation argument determines the edit item  or add new item.
     * If the itemId is positive, this method retrieves the information from the database and
     * allows the user to update it.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*   val id = navigationArgs.noteId
           if (id > 0) {
               viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                   item = selectedItem
                   bind(item)
               }
           } else {
               binding.saveAction.setOnClickListener {
                   addNewItem()
               }
           }*/
        binding.apply {
            btnStart.setOnClickListener {
                val action = MenuFragmentDirections.actionMenuFragmentToQuizFragment()
                findNavController().navigate(action)
            }
//            saveAction.setOnClickListener { updateItem() }
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
