package com.mr3y.ludi

import android.app.Application
import com.mr3y.ludi.shared.di.AndroidApplicationComponent
import com.mr3y.ludi.shared.di.create

class LudiApplication : Application() {

    val component: AndroidApplicationComponent by lazy(LazyThreadSafetyMode.NONE) {
        AndroidApplicationComponent::class.create(this)
    }
}
