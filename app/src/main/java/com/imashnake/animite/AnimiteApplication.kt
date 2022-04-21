package com.imashnake.animite

import android.app.Application
import com.imashnake.animite.data.Container
import dagger.hilt.android.HiltAndroidApp

/**
 * TODO: Kdoc.
 */
@HiltAndroidApp
class AnimiteApplication: Application() {
    val container = Container()
}
