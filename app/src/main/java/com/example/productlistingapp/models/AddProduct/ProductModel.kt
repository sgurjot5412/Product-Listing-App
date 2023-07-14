package com.example.productlistingapp.models.AddProduct

data class ProductModel(
    val message: String,
    val product_details: ProductDetails,
    val product_id: Int,
    val success: Boolean
)