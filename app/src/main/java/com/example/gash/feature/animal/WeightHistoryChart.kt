package com.example.gash.feature.animal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.WeightRecord
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashOrange
import com.example.gash.ui.theme.GashTextSecondary


@Composable
fun WeightHistoryChart(
    history: List<WeightRecord>,
    modifier: Modifier = Modifier
) {
    if (history.size < 2) {
        Box(modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_weight_chart),
                    contentDescription = null,
                    tint = GashGreen,
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = stringResource(R.string.animal_detail_weight_chart_not_enough_data),
                    color = GashTextSecondary,
                    fontSize = 12.sp.let { it }
                )
            }
        }
        return
    }

    val sorted = remember(history) { history.sortedBy { it.recordedAt } }
    val minWeight = sorted.minOf { it.weightKg }
    val maxWeight = sorted.maxOf { it.weightKg }
    val range = (maxWeight - minWeight).takeIf { it > 0.0 } ?: 1.0

    Canvas(modifier = modifier.fillMaxWidth().height(160.dp).padding(8.dp)) {
        val stepX = if (sorted.size > 1) size.width / (sorted.size - 1) else 0f
        val points = sorted.mapIndexed { index, record ->
            val x = index * stepX
            val normalized = ((record.weightKg - minWeight) / range).toFloat()
            val y = size.height - (normalized * size.height)
            Offset(x, y)
        }
        for (i in 0 until points.size - 1) {
            drawLine(color = GashGreen, start = points[i], end = points[i + 1], strokeWidth = 5f)
        }
        points.forEach { point ->
            drawCircle(color = GashOrange, radius = 7f, center = point)
        }
    }
}