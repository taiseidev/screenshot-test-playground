package com.example.screenshottest.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.screenshottest.ui.theme.AppTheme

/**
 * サンプル画面 - TopAppBar + PrimaryButton を組み合わせた画面
 * 共通コンポーネント変更時の影響確認用
 */
@Composable
fun SampleScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(title = "ログイン", onBackClick = onBackClick)
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "アカウントにログイン",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "メールアドレスとパスワードを入力してください",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
            Spacer(modifier = Modifier.height(32.dp))
            PrimaryButton(
                text = "ログイン",
                onClick = onLoginClick,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun SampleScreenPreview() {
    AppTheme(isDark = false) {
        SampleScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun SampleScreenDarkPreview() {
    AppTheme(isDark = true) {
        SampleScreen()
    }
}
