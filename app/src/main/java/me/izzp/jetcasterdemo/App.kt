package me.izzp.jetcasterdemo

import android.app.Application

class App : Application() {

    val feedRepository by lazy { FeedRepository(this) }

}