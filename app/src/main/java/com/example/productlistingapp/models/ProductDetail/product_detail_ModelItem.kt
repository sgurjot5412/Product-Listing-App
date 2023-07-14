package com.example.productlistingapp.models.ProductDetail

data class product_detail_ModelItem(
    val image: String,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)