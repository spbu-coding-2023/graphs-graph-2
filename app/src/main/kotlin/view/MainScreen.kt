package view

import MyAppTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import view.graph.GraphView
import view.tabScreen.AnalyzeTab
import view.tabScreen.FileControlTab
import view.tabScreen.GeneralTab
import view.tabScreen.SelectTabRow
import viewmodel.MainScreenViewModel


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun <D> MainScreen(viewmodel: MainScreenViewModel<D>) {
    MyAppTheme {
        // state for hover effect
        var isHovered by remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }

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
        // Hoverable box over the existing Surface
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {

            Surface(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(220.dp).height(50.dp)
                    .background(if (isHovered) Color.LightGray else Color.Gray)
                    .hoverable(interactionSource = interactionSource)
            ) {
                if (isHovered) {
                    Box(
                        modifier = Modifier.width(200.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = viewmodel.graphViewModel.graphType.value.replace(" ", "\nData type: "),
                            color = Color.Black
                        )
                    }
                } else {
                    Image(
                        painterResource("drawable/question.png"),
                        contentDescription = "Question Mark Icon",
                        modifier = Modifier.size(10.dp).padding(start = 50.dp)
                    )
                }
            }

            // Observe the interaction source to change the hover state
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is HoverInteraction.Enter -> {
                            isHovered = true
                        }

                        is HoverInteraction.Exit -> {
                            isHovered = false
                        }
                    }
                }
            }
        }
    }
}
