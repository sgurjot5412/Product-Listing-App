package com.example.productlistingapp.di

import com.example.productlistingapp.api.apiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun getRetrofit() : Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://app.getswipe.in/api/public/")
            .build()
    }

    @Provides
    @Singleton
    fun getApiInterface(retrofit: Retrofit): apiInterface{
        return retrofit.create(apiInterface::class.java)
    }

}