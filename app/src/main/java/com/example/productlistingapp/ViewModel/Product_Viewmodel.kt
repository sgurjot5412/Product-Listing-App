package com.example.productlistingapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productlistingapp.models.AddProduct.ProductModel
import com.example.productlistingapp.models.ProductDetail.product_detail_Model
import com.example.productlistingapp.repository.ProductRepository
import com.example.productlistingapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class Product_Viewmodel @Inject constructor(private val productRepository: ProductRepository): ViewModel() {

    val responseLiveData: LiveData<NetworkResult<product_detail_Model>>
    get() = productRepository.responseLiveData

    val productModelLiveData: LiveData<NetworkResult<ProductModel>>
        get() = productRepository.productLiveData


    fun getData() {
        viewModelScope.launch {
            productRepository.getData()
        }
    }
    fun getFilteredData(s: String){
        viewModelScope.launch{
            productRepository.getData()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun addData(name: String, type: String, price: String, tax: Double,image: MultipartBody.Part?){
        viewModelScope.launch {
            productRepository.addData(name, type, price, tax, image)
        }
    }
}