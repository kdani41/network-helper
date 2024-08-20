package com.kdani.network_helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdani.network_helper.network.SampleService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.kdani.core.network.NetworkResponse as coreNetwork

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val sampleService: SampleService,
) : ViewModel() {

    init {
        viewModelScope.launch {
            when (val res = sampleService.fetchData()) {
                is coreNetwork.ApiError -> Timber.e("error = ${res.throwable}")
                coreNetwork.Empty -> Timber.e("empty")
                is coreNetwork.Success -> Timber.i("sample res = $res")
            }
        }
    }
}