package com.rprikhodko.habrareader.home.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rprikhodko.habrareader.home.articles.data.PostsRepository
import com.rprikhodko.habrareader.common.data.dto.PostsPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repo: PostsRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val mainpage = MutableLiveData<PostsPage>()
    var job: Job? = null

    val loading = MutableLiveData<Boolean>()

    fun getMainPage() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repo.getArticles(1)
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