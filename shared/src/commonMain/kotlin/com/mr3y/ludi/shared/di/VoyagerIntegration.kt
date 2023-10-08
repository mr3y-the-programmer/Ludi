package com.mr3y.ludi.shared.di

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.screen.Screen

@Composable
expect inline fun <reified T : ScreenModel> Screen.getScreenModel(tag: String? = null): T

@Composable
expect inline fun <reified T : ScreenModel, reified P> Screen.getScreenModel(tag: String? = null, arg1: P): T