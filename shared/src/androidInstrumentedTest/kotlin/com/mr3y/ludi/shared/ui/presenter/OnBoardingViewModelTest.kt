package com.mr3y.ludi.shared.ui.presenter

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mr3y.ludi.shared.ui.datastore.PreferencesKeys
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class OnBoardingViewModelTest {

    @get:Rule(order = 0)
    val tempFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
    private val testScope = TestScope(testDispatcher)

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher(testDispatcher.scheduler))

    private val userPreferences: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope.backgroundScope,
        produceFile = { tempFolder.newFile("user_preferences_test.preferences_pb") }
    )

    private val protoDataStoreMutator = FakeProtoDataStoreMutator()

    private lateinit var sut: OnBoardingViewModel

    @Before
    fun setUp() {
        sut = OnBoardingViewModel(
            gamesRepository = FakeGamesRepository(),
            protoDataStore = protoDataStoreMutator,
            userPreferences = userPreferences
        )
        testScope.launch {
            protoDataStoreMutator.clearAll()
            userPreferences.edit { it.clear() }
        }
    }

    @Test
    fun user_completes_onboarding_successfully_onboarding_finishes() = testScope.runTest {
        userPreferences.edit {
            it[PreferencesKeys.OnBoardingScreenKey] = true
        }

        sut.completeOnboarding()
        advanceUntilIdle()

        expectThat(userPreferences.data.first()[PreferencesKeys.OnBoardingScreenKey]).isEqualTo(false)
    }

    @After
    fun teardown() {
        testScope.launch {
            protoDataStoreMutator.clearAll()
            userPreferences.edit { it.clear() }
        }
    }
}
