package com.rprikhodko.habrareader.ui.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rprikhodko.habrareader.data.dto.post.PostPreview
import com.rprikhodko.habrareader.data.usecase.GetArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articlesProvider: GetArticlesUseCase
) : ViewModel() {

    val data: LiveData<List<PostPreview>>
        get() = _data
    private val _data = MutableLiveData<List<PostPreview>>(emptyList())

    init {
        loadData(1)
    }

    private fun loadData(page: Int) {
        viewModelScope.launch {
            val result = articlesProvider.invoke(page)
            _data.postValue(result)
        }
    }
}