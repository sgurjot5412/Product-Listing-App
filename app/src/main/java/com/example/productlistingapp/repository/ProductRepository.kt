package com.example.productlistingapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.productlistingapp.api.apiInterface
import com.example.productlistingapp.models.AddProduct.ProductModel
import com.example.productlistingapp.models.ProductDetail.product_detail_Model
import com.example.productlistingapp.utils.NetworkResult
import okhttp3.MultipartBody
import javax.inject.Inject

class ProductRepository @Inject constructor(private val apiInterface: apiInterface) {

    private val _responseLiveData = MutableLiveData<NetworkResult<product_detail_Model>>()
    val responseLiveData: LiveData<NetworkResult<product_detail_Model>>
    get() = _responseLiveData

    private val _productLiveData = MutableLiveData<NetworkResult<ProductModel>>()
    val productLiveData: LiveData<NetworkResult<ProductModel>>
    get() = _productLiveData

    suspend fun getData(){
        _responseLiveData.postValue(NetworkResult.Loading())
        val response = apiInterface.getData()
        println(response.body().toString())
        if(response.isSuccessful && response.body() != null){
            _responseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!= null){
            _responseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
        else{
            _responseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun addData(name: String,type: String, price: String, tax: Double,image: MultipartBody.Part?  ){
        _productLiveData.postValue(NetworkResult.Loading())
        val response = apiInterface.addData(name,type,price,tax, image)
        println(response.body().toString())

        if(response.isSuccessful && response.body() != null){
            _productLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!= null){
            _productLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
        else{
            _productLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}