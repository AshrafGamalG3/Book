package com.example.bookapp.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.R
import com.example.bookapp.ui.auth.domain.usecase.AuthUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _navigate = MutableStateFlow<Int?>(null)
    val navigate: StateFlow<Int?> = _navigate

    companion object {
         val LOGIN_FRAGMENT = R.id.action_splashFragment_to_welcomeFragment
         val ADMIN_FRAGMENT = R.id.action_splashFragment_to_homeFragment
         val USER_FRAGMENT = R.id.action_splashFragment_to_userFragment
    }

    init {
        viewModelScope.launch {
            delay(3000) // 3-second splash delay
            checkUserStatus()
        }
    }

    private fun checkUserStatus() {
        val user = firebaseAuth.currentUser
        val isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false)

        viewModelScope.launch {
            if (user != null) {

                try {
                    val userRole = authUseCase.getUserById(user.uid)
                    when (userRole.data?.type) {
                        "admin" -> _navigate.emit(ADMIN_FRAGMENT)
                        "user" -> _navigate.emit(USER_FRAGMENT)
                        else -> _navigate.emit(LOGIN_FRAGMENT)
                    }
                } catch (e: Exception) {

                    _navigate.emit(LOGIN_FRAGMENT)
                }
            } else if (isUserLoggedIn) {
                _navigate.emit(LOGIN_FRAGMENT)
            } else {
                _navigate.emit(LOGIN_FRAGMENT)
            }
        }
    }

}
