package com.bangkit.sortify

import okhttp3.MultipartBody
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/predict")
    suspend fun postFoto(
        @Part file: MultipartBody.Part
    ): DataResponse
}