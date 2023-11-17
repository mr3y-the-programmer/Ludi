package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.di.annotations.DiscoverFeatureScope
import me.tatarka.inject.annotations.Component

@Component
@DiscoverFeatureScope
abstract class DiscoverFeatureComponent(
    @Component val parent: HostActivityComponent
) : SharedDiscoverFeatureComponent
