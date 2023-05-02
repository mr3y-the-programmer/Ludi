package com.mr3y.ludi.ui.presenter

/*
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class OnBoardingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testScope = TestScope(mainDispatcherRule.testDispatcher)
    private val context = ApplicationProvider.getApplicationContext<LudiApplication>()

    private lateinit var sut: OnBoardingViewModel
    private val repository = object : GamesRepository {
        override fun queryFreeGames(queryParameters: FreeGamesQueryParameters): Flow<Result<List<FreeGame>, Throwable>> {
            TODO("Not yet implemented")
        }

        override fun queryRichInfoGames(queryParameters: RichInfoGamesQueryParameters): Flow<Result<RichInfoGamesPage, Throwable>> {
            TODO("Not yet implemented")
        }
    }

    private val followedNewsDataSourcesStore = DataStoreFactory.create(
        serializer = FollowedNewsDataSourceSerializer,
        scope = testScope,
        produceFile = { context.dataStoreFile("followed_news_sources_test.pb")}
    )
    private val favGamesStore = DataStoreFactory.create(
        serializer = FavouriteGamesSerializer,
        scope = testScope,
        produceFile = { context.dataStoreFile("fav_games_testt.pb")}
    )

    @Before
    fun setUp() {
        sut = OnBoardingViewModel(
            gamesRepository = repository,
            favGamesStore = favGamesStore,
            followedNewsDataSourcesStore = followedNewsDataSourcesStore
        )
    }

    @Test
    fun whenUserFollowsDataSource_dataSourceIsSavedLocallyAndStateIsUpdated() = testScope.runTest {
        // given no followed data sources
        expectThat(sut.onboardingState.value.followedNewsDataSources).isEqualTo(emptyList())
        expectThat(sut.onboardingState.value.isUpdatingFollowedNewsDataSources).isEqualTo(false)

        // when
        sut.followNewsDataSource(NewsDataSource("Game spot", R.drawable.game_spot_logo, Source.GameSpot))

        // then
        expectThat(sut.onboardingState.value.followedNewsDataSources).isEqualTo(listOf(
            NewsDataSource("Game spot", R.drawable.game_spot_logo, Source.GameSpot)
        ))
        expectThat(sut.onboardingState.value.isUpdatingFollowedNewsDataSources).isEqualTo(false)
        val expectedStoredNewsDataSource = followedNewsDataSourcesStore.data.single().newsDataSourceList.map {
            NewsDataSource(name = it.name, drawableId = it.drawableId, type = Source.valueOf(it.type))
        }
        expectThat(expectedStoredNewsDataSource).isEqualTo(listOf(NewsDataSource("Game spot", R.drawable.game_spot_logo, Source.GameSpot)))
    }

    @Test
    fun whenUserUnfollowsDataSource_dataSourceIsSavedLocallyAndStateIsUpdated() {
        sut.unFollowNewsDataSource(NewsDataSource("Game spot", R.drawable.game_spot_logo, Source.GameSpot))
    }

    @After
    fun teardown() {
        testScope.launch {
            followedNewsDataSourcesStore.updateData {
                it.toBuilder().clear().build()
            }
            favGamesStore.updateData {
                it.toBuilder().clear().build()
            }
        }
    }
}*/
