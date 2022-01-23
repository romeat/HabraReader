package com.rprikhodko.habrareader.home.news

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rprikhodko.habrareader.common.data.dto.PostPreview
import com.rprikhodko.habrareader.data.network.SortBy
import com.rprikhodko.habrareader.home.articles.PostsPagingSource
import com.rprikhodko.habrareader.home.articles.data.PostsRepository
import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCase
import retrofit2.HttpException
import java.lang.Exception

class NewsPagingSource constructor(
    val useCase: GetNewsUseCase
) : PagingSource<Int, PostPreview>() {

    override fun getRefreshKey(state: PagingState<Int, PostPreview>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostPreview> {
        try {
            val pageNumber = params.key ?: 1
            val response = useCase.invoke(pageNumber)
            if (response.isSuccessful) {
                val articles = response.body()!!.articleRefs
                val pagesCount = response.body()!!.pagesCount
                val nextPageNumber = if (articles.isEmpty() || pagesCount == 1 || pageNumber == pagesCount) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                val sortedResult = articles.values.toList().filter{ it.postType == "news" }.sortedByDescending{ it.timePublished }
                return LoadResult.Page(sortedResult, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}