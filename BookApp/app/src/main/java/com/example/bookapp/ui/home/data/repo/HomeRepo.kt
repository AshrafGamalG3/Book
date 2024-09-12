package com.example.bookapp.ui.home.data.repo

import android.util.Log
import androidx.core.net.toUri
import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.ui.home.domain.repo.HomeIRepo
import com.example.bookapp.utils.Constants.BOOK_COLLECTION
import com.example.bookapp.utils.Constants.CATEGORY_COLLECTION
import com.example.bookapp.utils.Constants.FAVORITE_COLLECTION
import com.example.bookapp.utils.Constants.USER_COLLECTION
import com.example.bookapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : HomeIRepo {
    override suspend fun addCategory(category: CategoryModel): Resource<CategoryModel> {
        return try {
            db.collection(CATEGORY_COLLECTION).document(category.timestamp.toString()).set(category)
                .await()
            Resource.Success(category)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getCategories(): Resource<List<CategoryModel>> {
        return try {
            val categories = db.collection(CATEGORY_COLLECTION).get().await()
            Resource.Success(categories.toObjects(CategoryModel::class.java))

        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }

    }

    override suspend fun deleteCategory(category: CategoryModel): Resource<CategoryModel> {
        return try {
            val categoryRef = db.collection(CATEGORY_COLLECTION).document(category.timestamp.toString())
            val books = db.collection(BOOK_COLLECTION)
                .whereEqualTo("categoryName", category.categoryName)
                .get()
                .await()
            books.forEach { book ->
                val bookRef = db.collection(BOOK_COLLECTION).document(book.id)
                bookRef.delete().await()
            }
            categoryRef.delete().await()
            Resource.Success(category)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun addBook(bookModel: BookModel): Resource<BookModel> {
        return try {
            val bookRef = db.collection(BOOK_COLLECTION).document(bookModel.timestamp.toString())
            val pdfRef = storage.getReference(
                bookModel.pdfUrl
            )
            pdfRef.putFile(bookModel.pdfUrl.toUri()).await()
            bookRef.set(bookModel).await()
            Resource.Success(bookModel)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getBooksByCategory(category: String): Resource<List<BookModel>> {
        return try {
            Log.d("TAG", "Fetching books for category: $category")
            val books = db.collection(BOOK_COLLECTION)
                .whereEqualTo("categoryName", category)
                .get()
                .await()

            val bookList = books.toObjects(BookModel::class.java)
            Log.d("TAG", "Fetched books: $bookList")

            if (bookList.isNotEmpty()) {
                Resource.Success(bookList)
            } else {
                Resource.Error("No books found for the category: $category")
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error fetching books: ${e.message}")
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun deleteBook(bookModel: BookModel): Resource<BookModel> {
        return try {
            val bookRef = db.collection(BOOK_COLLECTION).document(bookModel.timestamp.toString())
            bookRef.delete().await()
            Resource.Success(bookModel)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun updateBook(bookModel: BookModel): Resource<BookModel> {
        return try {
            val bookRef = db.collection(BOOK_COLLECTION).document(bookModel.timestamp.toString())
            bookRef.set(bookModel).await()
            Resource.Success(bookModel)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun addFavorite(bookModel: BookModel): Resource<BookModel> {
        return try {
            val bookRef = db.collection(FAVORITE_COLLECTION).document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection(BOOK_COLLECTION).document(bookModel.timestamp.toString())
            bookRef.set(bookModel).await()
            Resource.Success(bookModel)
        }catch (e:Exception){
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun deleteFavorite(bookModel: BookModel): Resource<BookModel> {
        return try {
            val bookRef = db.collection(FAVORITE_COLLECTION).document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection(BOOK_COLLECTION).document(bookModel.timestamp.toString())
            bookRef.delete().await()
            Resource.Success(bookModel)
        }catch (e:Exception){
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun isFavorite(bookModel: BookModel): Resource<Boolean> {
        return try {
            val bookRef = db.collection(FAVORITE_COLLECTION).document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection(BOOK_COLLECTION).document(bookModel.timestamp.toString())
            val snapshot = bookRef.get().await()
            val isFavorite = snapshot.exists()
            Resource.Success(isFavorite)

        }catch (e:Exception){
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getFavoriteBooks(): Resource<List<BookModel>> {
        return try {
            val books = db.collection(FAVORITE_COLLECTION).document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection(BOOK_COLLECTION)
                .get()
                .await()
            Resource.Success(books.toObjects(BookModel::class.java))
        }catch (e:Exception){
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun updateProfile(user: User): Resource<User> {
        return try {
            val userRef = db.collection(USER_COLLECTION).document(FirebaseAuth.getInstance().currentUser!!.uid)
            userRef.set(user).await()
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }


}