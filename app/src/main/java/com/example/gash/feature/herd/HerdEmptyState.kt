package com.example.gash.feature.herd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashOrange
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun HerdEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(R.drawable.icon_herd_add),
            contentDescription = null,
            tint = GashGreen,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.herd_management_empty_title),
            color = GashTextPrimary,
            fontSize = 18.sp,
            fontWeight = Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.herd_management_empty_description),
            color = GashTextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(
//            onClick = onCreateHerd,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = GashOrange,
//                contentColor = Color.White
//            ),
//            shape = RoundedCornerShape(14.dp),
//            contentPadding = PaddingValues(
//                horizontal = 24.dp,
//                vertical = 12.dp
//            )
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = null
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Text(
//                text = stringResource(R.string.herd_management_create_first)
//            )
//        }
    }
}