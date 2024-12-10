package com.bangkit.sortify.repo

import com.bangkit.sortify.ApiService
import com.bangkit.sortify.DataResponse
import com.bangkit.sortify.pref.UserPreferences
import okhttp3.MultipartBody
import retrofit2.http.Multipart

class Repository(private val userPreferences: UserPreferences,private val apiService: ApiService) {
    suspend fun inputFoto(file: MultipartBody.Part): DataResponse {
        return apiService.postFoto(file)
    }

    companion object{
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            userPreferences: UserPreferences,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreferences, apiService)
            }.also { instance = it }
    }
}