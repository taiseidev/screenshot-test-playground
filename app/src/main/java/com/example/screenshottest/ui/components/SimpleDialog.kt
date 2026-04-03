package com.example.screenshottest.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.screenshottest.ui.theme.AppTheme

/**
 * 汎用ダイアログ - 確認/キャンセルの2ボタン構成
 */
@Composable
fun SimpleDialog(
    title: String,
    message: String,
    confirmText: String = "OK",
    dismissText: String = "キャンセル",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissText,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun SimpleDialogPreview() {
    AppTheme(isDark = false) {
        SimpleDialog(
            title = "退出しますか？",
            message = "通話から退出します。",
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SimpleDialogDarkPreview() {
    AppTheme(isDark = true) {
        SimpleDialog(
            title = "退出しますか？",
            message = "通話から退出します。",
            onConfirm = {},
            onDismiss = {},
        )
    }
}
