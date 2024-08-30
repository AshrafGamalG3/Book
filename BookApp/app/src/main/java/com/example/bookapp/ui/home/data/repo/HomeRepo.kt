package com.example.bookapp.ui.home.data.repo

import android.util.Log
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.ui.home.domain.repo.HomeIRepo
import com.example.bookapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepo @Inject constructor(private val db: FirebaseFirestore) : HomeIRepo {
    override suspend fun addCategory(category: CategoryModel): Resource<CategoryModel> {
        return try {
          db.collection("categories").document(category.timestamp.toString()).set(category).await()
            Resource.Success(category)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getCategories(): Resource<List<CategoryModel>> {
        return try {
       val categories= db.collection("categories").get().await()
            Resource.Success(categories.toObjects(CategoryModel::class.java))

        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }

    }

    override suspend fun deleteCategory(category: CategoryModel): Resource<CategoryModel> {
        return try {
            val categoryRef = db.collection("categories").document(category.timestamp.toString())
            categoryRef.delete().await()
            Resource.Success(category)
        }catch (e:Exception){
            Resource.Error(e.message.toString())
        }
    }
}