package com.oliver.siloker.presentation.feature.dashboard.profile.edit_employer

import androidx.lifecycle.ViewModel
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.UpdateEmployerRequest
import com.oliver.siloker.domain.repository.UserRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import com.oliver.siloker.presentation.util.updateState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class EditEmployerViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = BehaviorSubject.createDefault(EditEmployerState())
    val state = _state
        .doOnSubscribe {
            if (_state.value == EditEmployerState()) {
                getProfile()
            }
        }
        .share()
        .replay(1)
        .refCount(15000, TimeUnit.MILLISECONDS)

    private val _event = PublishSubject.create<EditEmployerEvent>()
    val event = _event.hide()

    private val compositeDisposable = CompositeDisposable()

    fun setCompanyName(value: String) {
        _state.updateState { it.copy(companyName = value) }
    }

    fun setCompanyWebsite(value: String) {
        _state.updateState { it.copy(companyWebsite = value) }
    }

    fun setPosition(value: String) {
        _state.updateState { it.copy(position = value) }
    }

    private fun getProfile() {
        _state.updateState { it.copy(isLoading = true) }

        val disposable = userRepository
            .getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.updateState { it.copy(isLoading = false) }
                result
                    .onSuccess { response ->
                        _state.updateState {
                            it.copy(
                                companyName = response.employer?.companyName ?: "",
                                companyWebsite = response.employer?.companyWebsite ?: "",
                                position = response.employer?.position ?: ""
                            )
                        }
                    }
                    .onError { _event.onNext(EditEmployerEvent.Error(it)) }
            }, {
                _state.updateState { it.copy(isLoading = false) }
                _event.onNext(EditEmployerEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    fun registerEmployer() {
        val request = UpdateEmployerRequest(
            companyName = _state.value?.companyName?.trim().toString(),
            position = _state.value?.position?.trim().toString(),
            companyWebsite = _state.value?.companyWebsite?.trim().toString()
        )

        _state.updateState { it.copy(isLoading = true) }

        val disposable = userRepository
            .updateEmployer(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.updateState { it.copy(isLoading = false) }
                result
                    .onSuccess { _event.onNext(EditEmployerEvent.Success) }
                    .onError { _event.onNext(EditEmployerEvent.Error(it)) }
            }, {
                _state.updateState { it.copy(isLoading = false) }
                _event.onNext(EditEmployerEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}