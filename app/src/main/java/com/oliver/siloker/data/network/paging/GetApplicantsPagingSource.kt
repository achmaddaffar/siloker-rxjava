package com.oliver.siloker.data.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oliver.siloker.data.mapper.toApplicantsDomain
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.model.response.GetApplicantsResponseDto
import com.oliver.siloker.data.util.getErrorResponse
import com.oliver.siloker.domain.model.response.ApplicantsResponseItem
import retrofit2.Response

class GetApplicantsPagingSource(
    private val onGetResponse: suspend (page: Int, size: Int) -> Response<BaseResponse<GetApplicantsResponseDto>>
): PagingSource<Int, ApplicantsResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, ApplicantsResponseItem>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(PAGE_SIZE)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApplicantsResponseItem> {
        try {
            val pageNumber = params.key ?: 1

            val response = onGetResponse(pageNumber, PAGE_SIZE)
            if (response.isSuccessful) {
                return LoadResult.Page(
                    data = response.body()?.data?.toApplicantsDomain() ?: emptyList(),
                    prevKey = response.body()?.data?.prevPage,
                    nextKey = response.body()?.data?.nextPage
                )
            }

            return LoadResult.Error(Throwable(response.errorBody().getErrorResponse()?.message))
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}