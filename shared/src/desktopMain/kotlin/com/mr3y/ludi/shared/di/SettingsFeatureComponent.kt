package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.di.annotations.SettingsFeatureScope
import me.tatarka.inject.annotations.Component

@Component
@SettingsFeatureScope
abstract class SettingsFeatureComponent(
    @Component val parent: HostWindowComponent
) : SharedSettingsFeatureComponent
