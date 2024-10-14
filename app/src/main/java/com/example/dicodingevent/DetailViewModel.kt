package com.example.dicodingevent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.DetailEventResponse
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    // LiveData untuk event detail
    private val _eventDetail = MutableLiveData<DetailEventResponse>()
    val eventDetail: LiveData<DetailEventResponse> get() = _eventDetail

    // LiveData untuk upcoming events
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    // Method untuk mendapatkan detail event
    fun fetchEventDetail(eventId: String) {
        viewModelScope.launch {
            try {
                val response: Response<DetailEventResponse> = apiService.getDetail(eventId)
                if (response.isSuccessful) {
                    _eventDetail.value = response.body()
                } else {
                    Log.e("DetailViewModel", "Error fetching event detail: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Exception: ${e.message}")
            }
        }
    }

    // Method untuk mendapatkan upcoming events
    fun fetchUpcomingEvents() {
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = apiService.getEvents(active = 1)
                if (response.isSuccessful) {
                    _upcomingEvents.value = response.body()?.listEvents ?: listOf()
                } else {
                    _upcomingEvents.value = listOf() // Data kosong jika gagal
                }
            } catch (e: Exception) {
                _upcomingEvents.value = listOf() // Pada kegagalan, beri data kosong
            }
        }
    }
}