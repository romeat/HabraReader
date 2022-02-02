package com.rprikhodko.habrareader.categories.companies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rprihodko.habrareader.common.dto.CompanyPreview
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCase
import retrofit2.HttpException
import java.lang.Exception

class CompaniesPagingSource constructor(
    private val useCase: GetCompaniesUseCase
) : PagingSource<Int, CompanyPreview>() {

    override fun getRefreshKey(state: PagingState<Int, CompanyPreview>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CompanyPreview> {
        try {
            val pageNumber = params.key ?: 1
            val response = useCase.invoke(pageNumber)
            if (response.isSuccessful) {
                val companies = response.body()!!.companyRefs
                val pagesCount = response.body()!!.pagesCount
                val nextPageNumber =
                    if (companies.isEmpty() || pagesCount == 1 || pageNumber == pagesCount) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                val result = companies.values.toList()
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