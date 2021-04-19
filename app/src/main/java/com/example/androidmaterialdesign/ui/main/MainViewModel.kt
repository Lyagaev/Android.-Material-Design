package com.example.androidmaterialdesign.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmaterialdesign.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel(
        private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
        private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
    ) :
        ViewModel() {

        fun getData(dateStr: String): LiveData<PictureOfTheDayData> {
            sendServerRequest(dateStr)
            return liveDataForViewToObserve
        }

        fun getDaysAgo(daysAgo: Int): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, daysAgo)
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(calendar.time)
        }

        private fun sendServerRequest(dateStr: String) {
            liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
            val apiKey: String = BuildConfig.NASA_API_KEY
            if (apiKey.isBlank()) {
                PictureOfTheDayData.Error(Throwable("Пустой ключ API"))
            } else {
                retrofitImpl.getRetrofitImpl().getPictureOfTheDay(dateStr, apiKey).enqueue(object :
                    Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable("Неизвестная ошибка"))
                            } else {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                    }
                })
            }
        }
    }
