package com.example.bookapp.ui.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.ui.auth.domain.usecase.AuthUseCase
import com.example.bookapp.utils.RegisterFieldsState
import com.example.bookapp.utils.RegisterValidation
import com.example.bookapp.utils.Resource
import com.example.bookapp.utils.validateConfirmPassword
import com.example.bookapp.utils.validateEmail
import com.example.bookapp.utils.validateName
import com.example.bookapp.utils.validatePassword
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: MutableStateFlow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    private val _login = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val login: MutableStateFlow<Resource<FirebaseUser>> = _login

    private val _resetPassword = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val resetPassword: MutableStateFlow<Resource<String>> = _resetPassword

    
    fun createAccountWithEmailAndPass(user: User) {
        if (checkValidationRegister(user, user.password, user.confirmPassword)) {
            viewModelScope.launch {
                _register.emit(Resource.Loading())
                val register = authUseCase.createAccountWithEmailAndPass(user, user.password)
                _register.emit(register)
            }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateEmail(user.email),
                validatePassword(user.password),
                validateName(user.name),
                validateConfirmPassword(
                    user.password, user.confirmPassword
                )
            )
            viewModelScope.launch {
                _validation.send(registerFieldsState)
            }
        }
    }

    fun resetPassword(email: String) {
        if (validateEmail(email) is RegisterValidation.Success) {
            viewModelScope.launch {
                _resetPassword.emit(Resource.Loading())
                val reset = authUseCase.resetPassword(email)
                _resetPassword.emit(reset)
            }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateEmail(email),
                validatePassword(""),
                validateName(""),
                validateConfirmPassword("", "")
            )
            viewModelScope.launch {
                _validation.send(registerFieldsState)
            }
        }
    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        if (checkValidationLogin(email, password)) {
            viewModelScope.launch {
                _login.emit(Resource.Loading())
                val login = authUseCase.loginWithEmailAndPass(email, password)
                _login.emit(login)
            }
        }

    }

    private fun checkValidationLogin(email: String, password: String): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        return emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

    }

    private fun checkValidationRegister(
        user: User,
        password: String,
        confirmPassword: String,
    ): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val nameValidation = validateName(user.name)
        val confirmPasswordValidation = validateConfirmPassword(password, confirmPassword)
        return emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
                && nameValidation is RegisterValidation.Success && confirmPasswordValidation is RegisterValidation.Success


    }


}