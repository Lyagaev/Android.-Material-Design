package com.example.androidmaterialdesign.model

sealed class PictureOfTheDayData {
    data class Success(val serverResponseData: List<PODServerResponseData>) : PictureOfTheDayData()
    data class Error(val error: Throwable) : PictureOfTheDayData()
    data class Loading(val progress: Int?) : PictureOfTheDayData()
}
