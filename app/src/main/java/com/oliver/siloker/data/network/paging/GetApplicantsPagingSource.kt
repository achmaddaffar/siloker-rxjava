package com.oliver.siloker.data.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oliver.siloker.data.mapper.toApplicantsDomain
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.model.response.GetApplicantsResponseDto
import com.oliver.siloker.data.util.getErrorResponse
import com.oliver.siloker.domain.model.response.ApplicantsResponseItem
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.rx3.await
import kotlinx.coroutines.rx3.rxSingle
import retrofit2.Response

class GetApplicantsPagingSource(
    private val getApplicants: (page: Int, size: Int) -> Single<Response<BaseResponse<GetApplicantsResponseDto>>>
) : PagingSource<Int, ApplicantsResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, ApplicantsResponseItem>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(PAGE_SIZE)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApplicantsResponseItem> {
        return rxSingle {
            getApplicants(params.key ?: 1, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .map { response ->
                    if (response.isSuccessful) {
                        LoadResult.Page(
                            data = response.body()?.data?.toApplicantsDomain() ?: emptyList(),
                            prevKey = response.body()?.data?.prevPage,
                            nextKey = response.body()?.data?.nextPage
                        )
                    } else {
                        LoadResult.Error(
                            Throwable(response.errorBody()?.getErrorResponse()?.message)
                        )
                    }
                }.blockingGet()
        }.await()
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}