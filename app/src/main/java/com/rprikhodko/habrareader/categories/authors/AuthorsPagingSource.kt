package com.rprikhodko.habrareader.categories.authors

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rprikhodko.habrareader.categories.authors.data.AuthorPreview
import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCase
import retrofit2.HttpException
import java.lang.Exception

class AuthorsPagingSource constructor(
    private val useCase: GetAuthorsUseCase
) : PagingSource<Int, AuthorPreview>() {

    override fun getRefreshKey(state: PagingState<Int, AuthorPreview>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AuthorPreview> {
        try {
            val pageNumber = params.key ?: 1
            val response = useCase.invoke(pageNumber)
            if(response.isSuccessful) {
                val authors = response.body()!!.authorRefs
                val pagesCount = response.body()!!.pagesCount
                val nextPageNumber = if (authors.isEmpty() || pagesCount == 1 || pageNumber == pagesCount) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                val result = authors.values.toList().sortedByDescending { it.rating }
                return LoadResult.Page(result, prevPageNumber, nextPageNumber)
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