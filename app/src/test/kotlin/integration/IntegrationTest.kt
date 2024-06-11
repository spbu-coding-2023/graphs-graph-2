package integration

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.runBlocking
import model.graphs.UndirectedGraph
import org.junit.Rule
import org.junit.Test
import view.components.FAQBox
import viewmodel.MainScreenViewModel


class IntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `graph is undirected, check its view and FAQ button`() {
        // I'm not sure if we can consider this as an integration test
        // Imho it's an integration test, due to UI's connection with VM + there is more than 2 components (view/vm, or front/back)

        // SETUP
        val interactionSource = MutableInteractionSource()
        val viewmodel = MainScreenViewModel(UndirectedGraph<Int>(),"UndirectedGraph")

        composeTestRule.setContent {
            FAQBox(interactionSource, viewmodel.graphType)
        }

        // UI TEST

        // Verify initial state (not hovered)
        composeTestRule.onNodeWithTag("FAQBoxNotHovered").assertExists()

        // Simulate hover enter by changing the state manually
        runBlocking {
            interactionSource.tryEmit(HoverInteraction.Enter())
        }

        // Verify hovered state
        composeTestRule.onNodeWithTag("FAQBoxHovered").assertExists()
        composeTestRule.onNodeWithTag("FAQBoxHovered").assertExists()
        composeTestRule.onNodeWithTag("HoveredText").assertTextEquals("UndirectedGraph")

        composeTestRule.onNodeWithTag("")

        // CHECK VM
        val edgeViewModels = viewmodel.graphViewModel._edgeViewModels
        val allEdgesDirected = edgeViewModels.all { it.value.isDirected() }

        assert(allEdgesDirected) { "Not all edges are directed" }
    }

    @Test
    fun `check import from DB and save with other name`() {

    }
}
