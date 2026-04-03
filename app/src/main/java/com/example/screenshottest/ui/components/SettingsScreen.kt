package com.example.screenshottest.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.screenshottest.ui.theme.AppTheme

/**
 * 設定画面 - TopAppBarのみ使用（PrimaryButtonは使わない）
 * Surface色の変更には影響を受けるが、Primary色の変更には影響しない
 */
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppTopBar(title = "設定", onBackClick = onBackClick)
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            SettingsItem(title = "通知", subtitle = "プッシュ通知の設定", hasSwitch = true)
            HorizontalDivider()
            SettingsItem(title = "テーマ", subtitle = "ダークモードの切り替え", hasSwitch = true)
            HorizontalDivider()
            SettingsItem(title = "言語", subtitle = "日本語")
            HorizontalDivider()
            SettingsItem(title = "バージョン", subtitle = "1.0.0")
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    hasSwitch: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
        if (hasSwitch) {
            Switch(checked = true, onCheckedChange = null)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun SettingsScreenPreview() {
    AppTheme(isDark = false) {
        SettingsScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun SettingsScreenDarkPreview() {
    AppTheme(isDark = true) {
        SettingsScreen()
    }
}
