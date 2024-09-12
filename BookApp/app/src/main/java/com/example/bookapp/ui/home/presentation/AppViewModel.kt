package com.example.bookapp.ui.home.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.ui.home.data.mapper.HomeMapper
import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.ui.home.domain.usecase.HomeUseCase
import com.example.bookapp.utils.BookValidationResult
import com.example.bookapp.utils.Resource
import com.example.bookapp.utils.ValidationResult
import com.example.bookapp.utils.validateCategory
import com.example.bookapp.utils.validateDescription
import com.example.bookapp.utils.validatePdfUri
import com.example.bookapp.utils.validateTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val mapper: HomeMapper,
) : ViewModel() {

    private var _addCategory = MutableStateFlow<Resource<CategoryModel>>(Resource.Unspecified())
    val addCategory: MutableStateFlow<Resource<CategoryModel>> = _addCategory

    private var _categories = MutableLiveData<Resource<List<CategoryModel>>>(Resource.Unspecified())
    val categories: MutableLiveData<Resource<List<CategoryModel>>> = _categories

    private var _deleteCategory = MutableStateFlow<Resource<CategoryModel>>(Resource.Unspecified())
    val deleteCategory: MutableStateFlow<Resource<CategoryModel>> = _deleteCategory

    private var _validBooks = Channel<BookValidationResult>()
    val validBooks = _validBooks.receiveAsFlow()

    private var _addBook = MutableStateFlow<Resource<BookModel>>(Resource.Unspecified())
    val addBook: MutableStateFlow<Resource<BookModel>> = _addBook

    private var _getAllBooksByCategory = MutableLiveData<Resource<List<BookModel>>>(Resource.Unspecified())
    val getAllBooksByCategory: MutableLiveData<Resource<List<BookModel>>> = _getAllBooksByCategory

    private var _updateBook = MutableStateFlow<Resource<BookModel>>(Resource.Unspecified())
    val updateBook: MutableStateFlow<Resource<BookModel>> = _updateBook

    private var _favoriteBook = MutableStateFlow<Resource<BookModel>>(Resource.Unspecified())
    val favoriteBook: MutableStateFlow<Resource<BookModel>> = _favoriteBook

    private var _isFavorite = MutableStateFlow<Resource<Boolean>>(Resource.Unspecified())
    val isFavorite: MutableStateFlow<Resource<Boolean>> = _isFavorite

    private var _getFavoriteBooks = MutableLiveData<Resource<List<BookModel>>>(Resource.Unspecified())
    val getFavoriteBooks: MutableLiveData<Resource<List<BookModel>>> = _getFavoriteBooks





    fun addCategory(category: CategoryModel) {
        viewModelScope.launch {
            _addCategory.emit(Resource.Loading())
            val result = homeUseCase.addCategory(category)
            _addCategory.emit(result)

        }
    }

    fun getCategories() {
        viewModelScope.launch {
            _categories.value = Resource.Loading()
            try {
                val result = homeUseCase.getCategories()
                val sortedResult = mapper.sortCategoriesByName(result.data ?: emptyList())
                _categories.value = Resource.Success(sortedResult)

            } catch (e: Exception) {


                _categories.value = Resource.Error(e.message.toString())

            }
        }
    }

    fun deleteCategory(category: CategoryModel) {
        viewModelScope.launch {
            _deleteCategory.emit(Resource.Loading())
            val result = homeUseCase.deleteCategory(category)
            _deleteCategory.emit(result)
        }
    }

    fun addBook(bookModel: BookModel) {
        if (checkValidation(bookModel.title, bookModel.description, bookModel.categoryName,bookModel.pdfUrl)) {
            viewModelScope.launch {
            _addBook.emit(Resource.Loading())
                val result = homeUseCase.addBook(bookModel)
                _addBook.emit(result)
            }
        } else {
            val validationResult = BookValidationResult(
                validateTitle(bookModel.title),
                validateDescription(bookModel.description),
                validateCategory(bookModel.categoryName),
                validatePdfUri(bookModel.pdfUrl)

            )
            viewModelScope.launch {
                _validBooks.send(validationResult)
            }
        }
    }

    private fun checkValidation(
        title: String,
        description: String,
        category: String,
        pdfUri: String,
    ): Boolean {
        val titleResult = validateTitle(title)
        val descriptionResult = validateDescription(description)
        val categoryResult = validateCategory(category)
        val pdfUriResult = validatePdfUri(pdfUri)

        return titleResult is ValidationResult.Success && descriptionResult is ValidationResult.Success && categoryResult is ValidationResult.Success && pdfUriResult is ValidationResult.Success
    }


    fun getAllBooksByCategory(categoryName: String){
        viewModelScope.launch {
            _getAllBooksByCategory.value = Resource.Loading()
            try {
                val result = homeUseCase.getBooksByCategory(categoryName)
                val sort=mapper.sortBooksByName(result.data?: emptyList())
                _getAllBooksByCategory.value = Resource.Success(sort)
                Log.e("TAG", "getAllBooksByCategory: ${result.data}")
                } catch (e: Exception) {
                _getAllBooksByCategory.value = Resource.Error(e.message.toString())
            }


        }
    }

    fun deleteBook(bookModel: BookModel){
        viewModelScope.launch {
            _addBook.emit(Resource.Loading())
            val result = homeUseCase.deleteBook(bookModel)
            _addBook.emit(result)
        }

    }
    fun updateBook(bookModel: BookModel){
        viewModelScope.launch {
            _updateBook.emit(Resource.Loading())
            val result = homeUseCase.updateBook(bookModel)
            _updateBook.emit(result)
        }
    }

    fun addFavorite(bookModel: BookModel){
        viewModelScope.launch {
            _favoriteBook.emit(Resource.Loading())
            val result = homeUseCase.changeFavorite(bookModel)
            _favoriteBook.emit(result)
        }
    }

    fun deleteFavorite(bookModel: BookModel){
        viewModelScope.launch {
            _favoriteBook.emit(Resource.Loading())
            val result = homeUseCase.deleteFavorite(bookModel)
            _favoriteBook.emit(result)
        }
    }

    fun isFavorite(bookModel: BookModel){
        viewModelScope.launch {
            _isFavorite.emit(Resource.Loading())
            val result = homeUseCase.isFavorite(bookModel)
            _isFavorite.emit(result)
        }

    }

    fun getFavoriteBooks(){
        viewModelScope.launch {
            _getFavoriteBooks.value = Resource.Loading()
            try {
                val result = homeUseCase.getFavoriteBooks()
                val sort=mapper.sortBooksByName(result.data?: emptyList())
                _getFavoriteBooks.value = Resource.Success(sort)
                Log.e("TAG", "getAllBooksByCategory: ${result.data}")
            } catch (e: Exception) {
                _getFavoriteBooks.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun updateProfile(user: User){
        viewModelScope.launch {
            homeUseCase.updateProfile(user)
        }


    }





}