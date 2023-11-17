package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.di.annotations.NewsFeatureScope
import me.tatarka.inject.annotations.Component

@Component
@NewsFeatureScope
abstract class NewsFeatureComponent(
    @Component val parent: HostActivityComponent
) : SharedNewsFeatureComponent
