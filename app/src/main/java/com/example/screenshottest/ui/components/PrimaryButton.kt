package com.example.screenshottest.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.screenshottest.ui.theme.AppTheme

/**
 * プライマリボタン - デザイントークンから色を取得
 * このコンポーネントの色を変更すると、使用している全画面に影響する
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPreview() {
    AppTheme(isDark = false) {
        PrimaryButton(text = "ログイン", onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonDisabledPreview() {
    AppTheme(isDark = false) {
        PrimaryButton(text = "ログイン", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonDarkPreview() {
    AppTheme(isDark = true) {
        PrimaryButton(text = "ログイン", onClick = {})
    }
}
