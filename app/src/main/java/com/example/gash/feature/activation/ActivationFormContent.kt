package com.example.gash.feature.activation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.ui.theme.GashError_RED
import com.example.gash.ui.theme.GashOrange
import com.example.gash.ui.theme.GashTextSecondary
import com.example.gash.ui.theme.GashText_DARKGREEN

@Composable
fun ActivationFormContent(
    uiState: ActivationUiState,
    onFarmNameChange: (String) -> Unit,
    onFarmIdChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onRequestCode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.gash_activation_lamb_typing),
                contentDescription = "gash_activation_lamb_typing",
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.activation_form_title),
                color = GashText_DARKGREEN,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            ActivationTextField(
                value = uiState.farmName,
                onValueChange = onFarmNameChange,
                placeholder = stringResource(R.string.activation_form_farmName),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(Modifier.height(12.dp))

            ActivationTextField(
                value = uiState.farmId,
                onValueChange = onFarmIdChange,
                placeholder = stringResource(R.string.activation_form_farmId),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
            )

            Spacer(Modifier.height(12.dp))

            ActivationTextField(
                value = uiState.phoneNumber,
                onValueChange = onPhoneNumberChange,
                placeholder = stringResource(R.string.activation_form_phoneNumber),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done)
            )

            uiState.formError?.let { error ->
                Spacer(Modifier.height(8.dp))
                Text(text = error.asString(), color = GashError_RED, fontSize = 13.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onRequestCode,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GashOrange, contentColor = Color.White)
            ) {
                Text(
                    text = stringResource(R.string.activation_form_requestCode),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(20.dp))

            Image(
                painter = painterResource(R.drawable.gash_icon_colored),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.height(42.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(8.dp))

            Text(text = stringResource(R.string.app_slogan), color = GashTextSecondary, fontSize = 11.sp)
        }
    }
}