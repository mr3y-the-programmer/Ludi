package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.di.annotations.OnboardingFeatureScope
import me.tatarka.inject.annotations.Component

@Component
@OnboardingFeatureScope
abstract class OnboardingFeatureComponent(
    @Component val parent: HostActivityComponent
) : SharedOnboardingFeatureComponent
