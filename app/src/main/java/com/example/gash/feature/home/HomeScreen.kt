package com.example.gash.feature.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gash.feature.herd.HerdManagementScreen
import com.example.gash.feature.weight.WeightScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNavigateToHerdDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = HomePage.OPERATIONS.ordinal) { HomePage.entries.size }
    val scope = rememberCoroutineScope()
    val currentPage = HomePage.entries[pagerState.currentPage]

    fun scrollTo(page: HomePage) {
        scope.launch { pagerState.animateScrollToPage(page.ordinal) }
    }

    BackHandler(enabled = currentPage != HomePage.OPERATIONS) {
        scrollTo(HomePage.OPERATIONS)
    }

    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader(
            uiState = uiState,
            currentPage = currentPage,
            onPageSelected = { scrollTo(it) }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            when (HomePage.entries[pageIndex]) {
                HomePage.WEIGHT -> WeightScreen()
                HomePage.OPERATIONS -> HomeOperationsPage()
                HomePage.HERD_MANAGEMENT -> HerdManagementScreen(onHerdClick = onNavigateToHerdDetail)
            }
        }
    }
}

@Composable
private fun HomeOperationsPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HomeOperationsGrid(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            onOperationClick = { /* TODO: با اولین عملیات واقعی وصل می‌شه */ }
        )
    }
}