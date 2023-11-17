package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.di.annotations.DealsFeatureScope
import me.tatarka.inject.annotations.Component

@Component
@DealsFeatureScope
abstract class DealsFeatureComponent(
    @Component val parent: HostWindowComponent
) : SharedDealsFeatureComponent
