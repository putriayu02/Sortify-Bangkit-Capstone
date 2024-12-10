package com.bangkit.sortify.di

import android.content.Context
import com.bangkit.sortify.ApiConfig
import com.bangkit.sortify.pref.UserPreferences
import com.bangkit.sortify.pref.dataStore
import com.bangkit.sortify.repo.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(pref, apiService)
    }
}