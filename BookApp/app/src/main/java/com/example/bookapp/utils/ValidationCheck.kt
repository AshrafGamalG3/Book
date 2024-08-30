package com.example.bookapp.utils

import android.util.Patterns
import org.intellij.lang.annotations.Pattern

fun validateEmail(email: String): RegisterValidation {
   if (email.isEmpty()){
      return RegisterValidation.Failed("Email cannot be empty")
   }
   if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
      return RegisterValidation.Failed("Wrong email Format")
   }

   return RegisterValidation.Success

}

fun validateName(name: String): RegisterValidation {
   return if (name.isNotBlank()) {
      RegisterValidation.Success
   } else {
      RegisterValidation.Failed("Name cannot be empty")
   }
}

fun validateConfirmPassword(password: String, confirmPassword: String): RegisterValidation {
   return if (password == confirmPassword) {
      RegisterValidation.Success
   } else {
      RegisterValidation.Failed("Passwords do not match")
   }
}
fun validatePassword(password:String):RegisterValidation{
   if (password.isEmpty()){
      return RegisterValidation.Failed("Password cannot be empty")
   }
   if (password.length<6){
      return RegisterValidation.Failed("Password should contains 6 char")

   }
   return RegisterValidation.Success
}

