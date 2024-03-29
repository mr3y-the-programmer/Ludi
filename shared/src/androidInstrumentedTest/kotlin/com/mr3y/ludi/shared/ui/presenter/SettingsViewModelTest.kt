package com.mr3y.ludi.shared.ui.presenter

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mr3y.ludi.shared.ui.presenter.model.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SettingsViewModelTest {

    @get:Rule(order = 0)
    val tempFolder = TemporaryFolder()

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private val testScope = TestScope(testDispatcher)

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val userPreferences: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope.backgroundScope,
        produceFile = { tempFolder.newFile("user_preferences_test.preferences_pb") }
    )

    private lateinit var sut: SettingsViewModel

    @Before
    fun setUp() {
        sut = SettingsViewModel(userPreferences)
        testScope.launch { userPreferences.edit { it.clear() } }
    }

    @Test
    @Ignore("")
    fun user_updates_preferences_new_preferences_are_saved() = testScope.runTest {
        backgroundScope.launch {
            sut.settingsState.collect()
        }
        expectThat(sut.settingsState.value).isEqualTo(SettingsViewModel.Initial)

        sut.setAppTheme(Theme.Dark)
        sut.enableUsingDynamicColor(false)

        advanceUntilIdle()

        expectThat(sut.settingsState.value).isEqualTo(SettingsViewModel.Initial.copy(selectedTheme = Theme.Dark, isUsingDynamicColor = false))
    }

    @After
    fun teardown() {
        testScope.launch { userPreferences.edit { it.clear() } }
    }
}
