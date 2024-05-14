package view

import MyAppTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import view.graph.GraphView
import view.tabScreen.*
import viewmodel.MainScreenViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <D> MainScreen(viewmodel: MainScreenViewModel<D>, showDialog: Boolean) {
    MyAppTheme {
        // Content of the main screen
        Row {
            // Column with tabs and content
            Column(
                modifier =
                    Modifier.width(360.dp)
                        .background(color = MaterialTheme.colors.surface)
                        .fillMaxHeight()
                        .clip(shape = RoundedCornerShape(10.dp))
            ) {
                // Tab row
                val pageState = rememberPagerState(pageCount = { 3 })
                val coroutineScope = rememberCoroutineScope()
                val tabs = listOf("General", "Analyze", "Save & Load")

                TabRow(
                    selectedTabIndex = pageState.currentPage,
                    contentColor = MaterialTheme.colors.surface,
                    backgroundColor = MaterialTheme.colors.secondary,
                    divider = {}, // to remove divider between
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier =
                                Modifier.tabIndicatorOffset(tabPositions[pageState.currentPage]),
                            height = 0.dp
                        )
                    },
                    modifier = Modifier.height(50.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        SelectTabRow(pageState, index, coroutineScope, title)
                    }
                }

                // Content corresponding to the selected tab
                HorizontalPager(state = pageState, userScrollEnabled = true) {
                    Column(
                        modifier =
                            Modifier.width(360.dp)
                                .background(MaterialTheme.colors.background)
                                .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        when (pageState.currentPage) {
                            0 -> GeneralTab(viewmodel.graphViewModel)
                            1 -> AnalyzeTab(viewmodel.graphViewModel)
                            2 -> FileControlTab(viewmodel.graphViewModel)
                        }
                    }
                }
            }
            Surface(modifier = Modifier.fillMaxSize()) { GraphView(viewmodel.graphViewModel) }
        }
        GraphInitDialogWindow(showDialog = showDialog)
    }
}
