package com.mr3y.ludi.ui.screens.deals

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mr3y.ludi.R
import com.mr3y.ludi.ui.components.LudiFilterChip
import com.mr3y.ludi.ui.presenter.model.DealStore
import com.mr3y.ludi.ui.presenter.model.DealsFiltersState
import com.mr3y.ludi.ui.presenter.model.GiveawayPlatform
import com.mr3y.ludi.ui.presenter.model.GiveawayStore
import com.mr3y.ludi.ui.presenter.model.GiveawaysFiltersState

internal enum class BottomSheetType {
    Deals,
    Giveaways
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun FiltersBottomSheet(
    dealsFiltersState: DealsFiltersState,
    giveawaysFiltersState: GiveawaysFiltersState,
    type: BottomSheetType,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCloseClicked: () -> Unit,
    onSelectingGiveawayPlatform: (GiveawayPlatform) -> Unit,
    onUnselectingGiveawayPlatform: (GiveawayPlatform) -> Unit,
    onSelectingDealStore: (DealStore) -> Unit,
    onUnselectingDealStore: (DealStore) -> Unit,
    onSelectingGiveawayStore: (GiveawayStore) -> Unit,
    onUnselectingGiveawayStore: (GiveawayStore) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = null,
        modifier = modifier
    ) {
        val chipModifier = Modifier
            .padding(vertical = 4.dp)
            .width(IntrinsicSize.Max)
            .animateContentSize()
        OutlinedButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.tertiary
            ),
            border = BorderStroke(0.dp, Color.Transparent),
            onClick = onCloseClicked
        ) {
            Text(
                text = stringResource(id = R.string.close_filters)
            )
        }
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            if (type == BottomSheetType.Deals) {
                Text(
                    text = "Stores:",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dealsFiltersState.allStores.forEach {
                        LudiFilterChip(
                            selected = it in dealsFiltersState.selectedStores,
                            label = it.label,
                            modifier = chipModifier,
                            onClick = {
                                if (it in dealsFiltersState.selectedStores) {
                                    onUnselectingDealStore(it)
                                } else {
                                    onSelectingDealStore(it)
                                }
                            }
                        )
                    }
                }
            } else {
                Text(
                    text = "Stores: ",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    giveawaysFiltersState.allStores.forEach {
                        LudiFilterChip(
                            selected = it in giveawaysFiltersState.selectedStores,
                            label = it.name,
                            modifier = chipModifier,
                            onClick = {
                                if (it in giveawaysFiltersState.selectedStores) {
                                    onUnselectingGiveawayStore(it)
                                } else {
                                    onSelectingGiveawayStore(it)
                                }
                            }
                        )
                    }
                }
                Text(
                    text = "Platforms: ",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    giveawaysFiltersState.allPlatforms.forEach {
                        LudiFilterChip(
                            selected = it in giveawaysFiltersState.selectedPlatforms,
                            label = it.name,
                            modifier = chipModifier,
                            onClick = {
                                if (it in giveawaysFiltersState.selectedPlatforms) {
                                    onUnselectingGiveawayPlatform(it)
                                } else {
                                    onSelectingGiveawayPlatform(it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
