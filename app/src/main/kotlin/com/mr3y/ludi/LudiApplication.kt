package com.mr3y.ludi

import android.app.Application
import com.mr3y.ludi.di.ApplicationComponent
import com.mr3y.ludi.di.create

class LudiApplication : Application() {

    val component: ApplicationComponent by lazy(LazyThreadSafetyMode.NONE) {
        ApplicationComponent::class.create(this)
    }
}
