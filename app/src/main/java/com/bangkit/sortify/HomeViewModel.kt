package com.bangkit.sortify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.sortify.repo.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException

class HomeViewModel(private val Repository: Repository): ViewModel()  {
    private val _predict = MutableLiveData<DataResponse>()
    val predict: LiveData<DataResponse> = _predict

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postFoto(file: MultipartBody.Part){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = Repository.inputFoto(file)
                _predict.postValue(response)
                _isLoading.postValue(false)
                Log.d("Post","ANjay")
            }catch (e: HttpException){
                _isLoading.postValue(false)
//                val jsonInString = e.response()?.errorBody()?.string()
//                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
//                val errorMessage = errorBody.message
                Log.d("Get History","${e.response()}")
            }
        }
    }


}