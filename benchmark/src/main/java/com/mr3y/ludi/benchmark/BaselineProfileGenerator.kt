package com.mr3y.ludi.benchmark

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        packageName = "com.mr3y.ludi",
        stableIterations = 2,
        maxIterations = 10,
        profileBlock = {
            // Start the app
            pressHome()
            startActivityAndWait()

            // simulate user journey through the app.
            waitForGenresToLoad()
            selectRandomGenres()
            moveToNextPage()

            waitForGamesToLoad()
            selectRandomGames()
            scrollGamesContainerHorizontally()
            moveToNextPage()

            waitForDataSources()
            moveToNextPage()
        }
    )

    fun MacrobenchmarkScope.waitForGenresToLoad() {
        device.wait(Until.gone(By.res("onboarding:genres:loadingWheel")), 10_000L)
        device.wait(Until.hasObject(By.res("onboarding:genres:content")), 1000L)
    }

    fun MacrobenchmarkScope.selectRandomGenres() {
        val genres = device.findObject(By.res("onboarding:genres:content")).children
        var i = 0
        while (i < 3) {
            val genre = genres.random()
            if (genre.isSelected) {
                continue
            }
            genre.click()
            device.waitForIdle()
            i++
        }
    }

    fun MacrobenchmarkScope.waitForGamesToLoad() {
        device.wait(Until.gone(By.res("onboarding:games:gameLoading")), 10_000L)
        device.wait(Until.hasObject(By.res("onboarding:games:gameContent")), 1000L)
    }

    fun MacrobenchmarkScope.selectRandomGames() {
        val gamesContainer = device.waitForObject(By.res("onboarding:games:suggestedGames"))

        // Set gesture margin from sides not to trigger system gesture navigation
        val horizontalMargin = 10 * gamesContainer.visibleBounds.width() / 100
        gamesContainer.setGestureMargins(horizontalMargin, 0, horizontalMargin, 0)
        val games = gamesContainer.children.filter { it.resourceName == "onboarding:games:gameContent" }
        repeat(3) {
            games.random().click()
            device.waitForIdle()
        }
    }

    fun MacrobenchmarkScope.scrollGamesContainerHorizontally() {
        val gamesContainer = device.waitForObject(By.res("onboarding:games:suggestedGames"))
        // Set gesture margin from sides not to trigger system gesture navigation
        gamesContainer.setGestureMargin(device.displayWidth / 5)
        gamesContainer.fling(Direction.RIGHT)
        device.waitForIdle()
        gamesContainer.fling(Direction.LEFT)
    }

    fun MacrobenchmarkScope.waitForDataSources() {
        device.wait(Until.hasObject(By.res("onboarding:datasources:content")), 10_000L)
    }

    fun MacrobenchmarkScope.moveToNextPage() {
        device.findObject(By.res("onboarding:fab")).click()
        device.waitForIdle()
    }
}