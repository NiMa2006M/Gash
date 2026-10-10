package com.example.gash.feature.herd

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gash.R
import com.example.gash.core.ui.components.SearchField

@Composable
fun HerdSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchField(
        query = query,
        onQueryChange = onQueryChange,
        placeholder = stringResource(R.string.herd_detail_search_hint),
        clearContentDescription = stringResource(R.string.common_btn_clear),
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp)
    )
}