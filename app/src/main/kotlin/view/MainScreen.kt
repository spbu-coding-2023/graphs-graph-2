@file:OptIn(ExperimentalFoundationApi::class)

package view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import viewmodel.MainScreenViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <D> MainScreen(viewmodel: MainScreenViewModel<D>) {

    // here we will have layout type shit
    Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
        Column(
            modifier = Modifier.width(360.dp)
                .background(color = Color.LightGray, shape = RoundedCornerShape(20.dp))
                .fillMaxHeight().clip(shape = RoundedCornerShape(20.dp))
                // TODO: make it rounded only from right side
        ) {

            val pageState = rememberPagerState( pageCount = { 3 } )
            val coroutineScope = rememberCoroutineScope()
            val tabs = listOf("General", "Analyze", " File Control")

            TabRow(
                selectedTabIndex = pageState.currentPage,
                contentColor = Color.Red,
                backgroundColor = Color.Gray,
                divider = {}, // to remove divider between
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pageState.currentPage]),
                        height = 0.dp
                    )
                },
                modifier = Modifier.height(50.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pageState.currentPage == index,
                        onClick = { coroutineScope.launch { pageState.animateScrollToPage(index) } },
                        modifier = Modifier.padding(0.dp),

                        content = {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (pageState.currentPage == index) Color.Magenta
                                        else Color.Transparent
                                    ).padding(10.dp).height(30.dp).width(120.dp).align(Alignment.CenterHorizontally),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = title,
                                    textAlign = TextAlign.Center,
                                    color = if (pageState.currentPage == index) Color.White else Color.Black // Set text color for selected and unselected tabs
                                )
                            }
                        }
                    )
                }
            }
            HorizontalPager(state = pageState, userScrollEnabled = false) {
                page ->
                Column(
                    modifier = Modifier.width(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Hello from page: $page")
                }
            }
        }
    }

    Surface {
//        GraphView(viewmodel.graphViewModel)
    }
}
