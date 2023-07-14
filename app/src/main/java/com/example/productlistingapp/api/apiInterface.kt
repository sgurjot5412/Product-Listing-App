package com.example.productlistingapp.api

import com.example.productlistingapp.models.AddProduct.ProductModel
import com.example.productlistingapp.models.ProductDetail.product_detail_Model
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface apiInterface {

    @GET("get")
    suspend fun getData(): Response<product_detail_Model>

    @Multipart
    @POST("add")
    suspend fun addData(
        @Part("product_name") product_name: String,
        @Part("product_type") product_type: String,
        @Part("price") price: String,
        @Part("tax") tax: Double,
        @Part image: MultipartBody.Part?,

        ): Response<ProductModel>

}