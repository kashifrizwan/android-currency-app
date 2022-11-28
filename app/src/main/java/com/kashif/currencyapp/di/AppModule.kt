package com.kashif.currencyapp.di

import com.kashif.currencyapp.network.RetrofitApi
import com.kashif.currencyapp.network.RetrofitBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Reusable
    fun provideRetrofitApi(retrofitBuilder: RetrofitBuilder): RetrofitApi {
        return retrofitBuilder.retrofit.create(RetrofitApi::class.java)
    }
}
