package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.LudiSharedState

interface SharedApplicationComponent :
    CoroutinesComponent,
    DataStoreComponent,
    LoggingComponent,
    NetworkComponent,
    SharedDatabaseComponent,
    RepositoriesComponent,
    RESTfulDataSourcesComponent,
    RSSFeedDataSourcesComponent {
    val appState: LudiSharedState
}
