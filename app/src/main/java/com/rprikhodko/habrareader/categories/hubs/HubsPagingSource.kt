package com.rprikhodko.habrareader.categories.hubs

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rprihodko.habrareader.common.dto.HubPreview
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCase
import retrofit2.HttpException
import java.lang.Exception

class HubsPagingSource constructor(
    private val useCase: GetHubsUseCase
) : PagingSource<Int, HubPreview>() {

    override fun getRefreshKey(state: PagingState<Int, HubPreview>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HubPreview> {
        try {
            val pageNumber = params.key ?: 1
            val response = useCase.invoke(pageNumber)
            if(response.isSuccessful) {
                val hubs = response.body()!!.hubRefs
                val pagesCount = response.body()!!.pagesCount
                val nextPageNumber = if (hubs.isEmpty() || pagesCount == 1 || pageNumber == pagesCount) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                val result = hubs.values.toList().sortedByDescending { it.statistics.rating }
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