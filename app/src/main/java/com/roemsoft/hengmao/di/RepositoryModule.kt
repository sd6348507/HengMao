package com.roemsoft.hengmao.di

import com.roemsoft.hengmao.api.ApiService
import com.roemsoft.hengmao.repository.AppRepository

object RepositoryModule {

    private val api: ApiService by lazy { provideApiService() }

    val repository: AppRepository by lazy { provideAppRepository() }

    private fun provideApiService(): ApiService {
        return NetworkModule.instance.retrofit.create(ApiService::class.java)
    }

    private fun provideAppRepository(): AppRepository {
        return AppRepository(api)
    }
}