package com.oliver.siloker.data.repository

import android.app.Application
import android.net.Uri
import com.oliver.siloker.data.mapper.toEmployerDto
import com.oliver.siloker.data.mapper.toGetProfileDomain
import com.oliver.siloker.data.mapper.toJobSeekerDto
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.service.UserService
import com.oliver.siloker.data.pref.SiLokerPreference
import com.oliver.siloker.data.util.getResponseRaw
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.UpdateEmployerRequest
import com.oliver.siloker.domain.model.request.UpdateJobSeekerRequest
import com.oliver.siloker.domain.model.response.GetProfileResponse
import com.oliver.siloker.domain.repository.UserRepository
import com.oliver.siloker.domain.util.FileUtil
import com.oliver.siloker.domain.util.Result
import com.oliver.siloker.domain.util.map
import com.oliver.siloker.domain.util.onSuccess
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class UserRepositoryImpl(
    private val userService: UserService,
    private val preference: SiLokerPreference,
    private val application: Application
) : UserRepository {

    override fun getProfile(): Single<Result<GetProfileResponse, NetworkError>> =
        Single.defer {
            userService.getProfile()
                .map { response ->
                    getResponseRaw(response)
                        .onSuccess {
                            preference.putEmployerId(it.data.employer?.id ?: -1)
                            preference.putJobSeekerId(it.data.jobSeeker?.id ?: -1)
                        }
                        .map { it.data.toGetProfileDomain() }
                }
                .subscribeOn(Schedulers.io())
        }

    override fun uploadProfilePicture(uri: Uri): Single<Result<BaseResponse<Boolean>, NetworkError>> =
        Single.defer {
            val file = FileUtil.uriToFile(uri, application)
            val part = MultipartBody.Part.createFormData(
                "profile_picture",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )

            userService.updateProfilePicture(part)
                .map { response -> getResponseRaw(response) }
                .subscribeOn(Schedulers.io())
        }

    override fun updateJobSeeker(request: UpdateJobSeekerRequest): Single<Result<BaseResponse<Boolean>, NetworkError>> =
        Single.defer {
            userService.updateJobSeeker(request.toJobSeekerDto())
                .map { response -> getResponseRaw(response) }
                .subscribeOn(Schedulers.io())
        }

    override fun updateEmployer(request: UpdateEmployerRequest): Single<Result<BaseResponse<Boolean>, NetworkError>> =
        Single.defer {
            userService.updateEmployer(request.toEmployerDto())
                .map { response -> getResponseRaw(response) }
                .subscribeOn(Schedulers.io())
        }

    override fun getEmployerId(): Long = preference.getEmployerId()

    override fun getJobSeekerId(): Long = preference.getJobSeekerId()
}