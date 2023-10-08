package com.mr3y.ludi.shared.di

interface SharedApplicationComponent :
    CoroutinesComponent,
    DataStoreComponent,
    LoggingComponent,
    NetworkComponent,
    RepositoriesComponent,
    RESTfulDataSourcesComponent,
    RSSFeedDataSourcesComponent,
    UseCasesComponent
{

}