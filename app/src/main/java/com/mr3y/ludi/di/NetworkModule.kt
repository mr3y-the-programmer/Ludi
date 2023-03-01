package com.mr3y.ludi.di

import android.content.Context
import com.prof.rssparser.Parser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object NetworkModule {

    @ActivityRetainedScoped
    @Provides
    fun provideRssParserInstance(@ActivityContext context: Context): Parser {
        return Parser.Builder()
            .context(context)
            .cacheExpirationMillis(8L * 60L * 60L * 1000L)
            .build()
    }
}