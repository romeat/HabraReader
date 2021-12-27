package com.rprikhodko.habrareader.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rprikhodko.habrareader.MainRepository
import com.rprikhodko.habrareader.data.dto.post.PostsPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repo: MainRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val mainpage = MutableLiveData<PostsPage>()
    var job: Job? = null

    val loading = MutableLiveData<Boolean>()

    fun getMainPage() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repo.getMain(1)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    mainpage.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }
}