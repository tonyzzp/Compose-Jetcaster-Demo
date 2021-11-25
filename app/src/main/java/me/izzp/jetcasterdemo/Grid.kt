package me.izzp.jetcasterdemo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun Grid(
    cells: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val itemSize = constraints.maxWidth / cells
        val childConstraints = Constraints.fixed(itemSize, itemSize)
        val placeables = measurables.map { it.measure(childConstraints) }
        val width = constraints.maxWidth
        val height =
            constraints.constrainHeight(itemSize * ceil(measurables.size.toFloat() / cells).toInt())
        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    (index % cells) * itemSize,
                    floor(index.toFloat() / cells).toInt() * itemSize,
                )
            }
        }
    }
}