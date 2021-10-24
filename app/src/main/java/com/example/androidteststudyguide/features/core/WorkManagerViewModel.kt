package com.example.androidteststudyguide.features.core

import android.app.Application

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*


class WorkManagerViewModel(application: Application) : ViewModel() {


    val workManager = WorkManager.getInstance(application)

    val statusLiveData1: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData("TAG_FOR_LIVEDATA")


    val statusLiveData2: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData("CANCELABLE_UNIQUE_WORK")


    fun cancelWork(){
        workManager.cancelUniqueWork("CANCELABLE_UNIQUE_WORK")
    }

    class WorkManagerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(WorkManagerViewModel::class.java)) {
                WorkManagerViewModel(application) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
