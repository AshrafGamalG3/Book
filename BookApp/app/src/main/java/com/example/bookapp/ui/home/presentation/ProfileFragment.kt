package com.example.bookapp.ui.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bookapp.App.Companion.formatDate
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentProfileBinding
import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.ui.auth.presentation.AuthViewModel
import com.example.bookapp.ui.home.presentation.adapter.BookAdapter
import com.example.bookapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {
  private var _binding :FragmentProfileBinding ?=null
    private val binding get() = _binding!!
    private val appViewModel: AppViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val bookAdapter = BookAdapter()
    private var user: User?=null
    var name:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        onClick()
        getUserData()
        observeUserData()
        getFavorites()
        favoriteBooksObserver()
    }

    private fun onClick(){
        binding.editProfile.setOnClickListener {
          val action=ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(user!!)
            findNavController().navigate(action)
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getFavorites(){
        appViewModel.getFavoriteBooks()
    }
    private fun getUserData(){
        authViewModel.getUserById(FirebaseAuth.getInstance().currentUser?.uid.toString())
    }
    private fun observeUserData(){
       lifecycleScope.launch {
           lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
               authViewModel.user.collect{
                   when(it){
                       is Resource.Loading ->{

                       }
                       is Resource.Success ->{
                           user=it.data
                           if (it.data?.type == "admin"){
                               binding.booksRecyclerView.visibility=View.GONE
                           }
                           binding.imageProfile.setImageURI(it.data?.imagePath?.toUri())
                           binding.name.text = it.data?.name
                           name=it.data?.name
                           binding.email.text = it.data?.email
                           binding.accountNum.text=it.data?.type
                           binding.memberNum.text=formatDate(it.data?.timestamp!!)

                       }
                       is Resource.Error ->{

                       }
                       else ->{
                           Unit
                       }
                   }
               }
           }
       }
    }
    private fun favoriteBooksObserver(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                appViewModel.getFavoriteBooks.observe(viewLifecycleOwner){
                    when(it){
                        is Resource.Success ->{
                            bookAdapter.type="favorite"
                            bookAdapter.books=it.data!!
                            binding.booksRecyclerView.adapter=bookAdapter
                            binding.favoriteNum.text=it.data.size.toString()
                        }

                        else ->Unit
                    }

                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}