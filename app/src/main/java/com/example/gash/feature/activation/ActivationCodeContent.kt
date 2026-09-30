package com.example.gash.feature.activation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashTextSecondary


@Composable
fun ActivationCodeContent(
    uiState: ActivationUiState,
    onCodeChange: (String) -> Unit,
    onCodeFocus: () -> Unit,
    onSubmitCode: () -> Unit,
    onResendCode: () -> Unit,
    onEditClick: () -> Unit
) {
    val illustrationRes = if (uiState.isCodeError) {
        R.drawable.gash_activation_lamb_confused
    } else {
        R.drawable.gash_activation_lamb_letter
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(illustrationRes),
                contentDescription = "gash_activation_lamb",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.activation_code_title),
                modifier = Modifier.fillMaxWidth(),
                color = GashGreen,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start

            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.activation_code_codeSendTo,
                        uiState.phoneNumber
                    ),
                    color = GashTextSecondary,
                    fontSize = 12.sp,
                )
                Spacer(Modifier.weight(1f))
                TextButton(
                    onClick = onEditClick
                ) {
                    Text(
                        text = stringResource(R.string.common_btn_edit)
                    )

                }
            }

            Spacer(Modifier.height(8.dp))

            if (uiState.isCodeError) {
                Text(
                    text = stringResource(
                        R.string.activation_code_wrongCode
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    color = androidx.compose.material3.MaterialTheme
                        .colorScheme
                        .error,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(24.dp))
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                ActivationCodeInput(
                    code = uiState.enteredCode,
                    isError = uiState.isCodeError,
                    onCodeChange = onCodeChange,
                    onFocus = onCodeFocus
                )

            }

            Spacer(Modifier.height(16.dp))

            if (uiState.remainingSeconds > 0) {

                Text(
                    text = stringResource(R.string.activation_code_codeRemainingTime,uiState.remainingSeconds / 60, uiState.remainingSeconds % 60)
                )

            } else {

                TextButton(
                    onClick = onResendCode
                ) {
                    Text(text = stringResource(R.string.activation_code_resendCode))
                }
            }

            Spacer(Modifier.height(32.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSubmitCode,
                enabled = uiState.enteredCode.length == ActivationViewModel.CODE_LENGTH &&
                        !uiState.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(text = stringResource(R.string.common_btn_login))
                }
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