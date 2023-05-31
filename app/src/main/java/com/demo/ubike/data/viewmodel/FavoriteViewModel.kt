package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.usecase.GetAllFavoriteUseCase
import com.demo.ubike.usecase.RemoveFavoriteUseCase
import com.demo.ubike.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase,
    private val favoriteRemoveUseCase: RemoveFavoriteUseCase,
) : BaseViewModel() {
    private val _favoriteList = MutableLiveData<List<FavoriteEntity>>()
    val favoriteList: LiveData<List<FavoriteEntity>> = _favoriteList

    private val _favoriteRemoved = MutableLiveData<Event<Boolean>>()
    val favoriteRemoved: LiveData<Event<Boolean>> = _favoriteRemoved

    fun getAllFavorite() {
        getAllFavoriteUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _favoriteList.value = it
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }

    fun removeFavorite(stationUid: String) {
        favoriteRemoveUseCase(stationUid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _favoriteRemoved.value = Event(true)
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }
}