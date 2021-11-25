package me.izzp.jetcasterdemo.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.izzp.jetcasterdemo.FeedRepository

class HomeViewModel(private val feedRepository: FeedRepository) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.loading())

    val homeState: StateFlow<HomeState>
        get() = _homeState

    init {
        reload()
    }

    fun reload() {
        _homeState.value = HomeState.loading()
        viewModelScope.launch {
            val feeds = feedRepository.fetch()
            if (feeds.isEmpty()) {
                _homeState.value = HomeState.failed()
            } else {
                _homeState.value = HomeState.content(feeds)
            }
        }
    }

}