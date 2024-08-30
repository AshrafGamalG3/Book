package com.example.bookapp.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.R
import com.example.bookapp.ui.home.presentation.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SplashViewModel @Inject constructor(

    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate


    companion object{
         val LOGIN_FRAGMENT = R.id.action_splashFragment_to_welcomeFragment
        const val HOME_FRAGMENT=23
    }

    init {
        val isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false)
        val user = firebaseAuth.currentUser


            if (user != null) {
                viewModelScope.launch {
                    _navigate.emit(HOME_FRAGMENT)
                }
            }
        else if (isUserLoggedIn) {
                viewModelScope.launch {
                    _navigate.emit(LOGIN_FRAGMENT)
                }
            } else {
               Unit
            }
    }

    fun startSplash(){
        sharedPreferences.edit().putBoolean("isUserLoggedIn", true).apply()
    }
}