package com.example.bookapp.ui.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.ui.home.data.mapper.HomeMapper
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.ui.home.domain.usecase.HomeUseCase
import com.example.bookapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUseCase: HomeUseCase,private val mapper: HomeMapper) : ViewModel() {

    private var _addCategory = MutableStateFlow<Resource<CategoryModel>>(Resource.Unspecified())
    val addCategory: MutableStateFlow<Resource<CategoryModel>> = _addCategory

    private var _categories = MutableLiveData<Resource<List<CategoryModel>>>(Resource.Unspecified())
    val categories: MutableLiveData<Resource<List<CategoryModel>>> = _categories

    private var _deleteCategory = MutableStateFlow<Resource<CategoryModel>>(Resource.Unspecified())
    val deleteCategory: MutableStateFlow<Resource<CategoryModel>> = _deleteCategory


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

}