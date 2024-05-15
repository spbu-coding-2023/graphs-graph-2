package view.tabScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectTabRow(
    currentPageState: PagerState,
    index: Int,
    coroutineScope: CoroutineScope,
    title: String
) {
    Tab(
        selected = currentPageState.currentPage == index,
        onClick = { coroutineScope.launch { currentPageState.animateScrollToPage(index) } },
        modifier = Modifier,
        content = {
            Box(
                modifier =
                Modifier.background(
                    if (currentPageState.currentPage == index) MaterialTheme.colors.primary
                    else Color.Transparent
                )
                    .padding(10.dp)
                    .height(30.dp)
                    .width(120.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    color = if (currentPageState.currentPage == index) MaterialTheme.colors.surface else Color.Black
                    // Set text color for selected and unselected tabs
                )
            }
        }
    )
}
