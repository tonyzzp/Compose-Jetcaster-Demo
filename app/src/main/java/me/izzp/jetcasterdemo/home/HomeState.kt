package me.izzp.jetcasterdemo.home

import me.izzp.jetcasterdemo.Feed

sealed interface HomeState {

    object HomeStateLoading : HomeState

    object HomeStateFailed : HomeState

    class HomeStateContent(
        val feeds: List<Feed>
    ) : HomeState

    companion object {
        fun failed() = HomeStateFailed
        fun loading() = HomeStateLoading
        fun content(feeds: List<Feed>) = HomeStateContent(feeds)
    }
}