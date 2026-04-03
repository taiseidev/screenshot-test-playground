package com.example.screenshottest.screenshot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.example.screenshottest.ui.components.AppTopBar
import com.example.screenshottest.ui.components.LoginScreen
import com.example.screenshottest.ui.components.PrimaryButton
import com.example.screenshottest.ui.components.ProfileScreen
import com.example.screenshottest.ui.components.SampleScreen
import com.example.screenshottest.ui.components.SettingsScreen
import com.example.screenshottest.ui.components.SimpleDialog
import com.example.screenshottest.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Compose UIコンポーネントのスクリーンショットテスト
 *
 * - Roborazziでスクショ撮影（エミュレータ不要）
 * - ライト/ダーク両テーマを自動撮影
 * - record → compare で差分画像を生成
 *
 * コマンド:
 *   ./gradlew recordRoborazziDebug   # ベースライン撮影
 *   ./gradlew compareRoborazziDebug  # 差分検出
 *   ./gradlew verifyRoborazziDebug   # 差分があればテスト失敗
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class ComponentScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // --- PrimaryButton ---

    @Test
    fun primaryButton_light() {
        composeTestRule.setContent {
            AppTheme(isDark = false) {
                Column(modifier = Modifier.padding(16.dp)) {
                    PrimaryButton(text = "ログイン", onClick = {})
                    Spacer(modifier = Modifier.height(8.dp))
                    PrimaryButton(text = "送信", onClick = {}, enabled = false)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/PrimaryButton_light.png",
        )
    }

    @Test
    fun primaryButton_dark() {
        composeTestRule.setContent {
            AppTheme(isDark = true) {
                Column(modifier = Modifier.padding(16.dp)) {
                    PrimaryButton(text = "ログイン", onClick = {})
                    Spacer(modifier = Modifier.height(8.dp))
                    PrimaryButton(text = "送信", onClick = {}, enabled = false)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/PrimaryButton_dark.png",
        )
    }

    // --- AppTopBar ---

    @Test
    fun topAppBar_light() {
        composeTestRule.setContent {
            AppTheme(isDark = false) {
                AppTopBar(title = "設定", onBackClick = {})
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/TopAppBar_light.png",
        )
    }

    @Test
    fun topAppBar_dark() {
        composeTestRule.setContent {
            AppTheme(isDark = true) {
                AppTopBar(title = "設定", onBackClick = {})
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/TopAppBar_dark.png",
        )
    }

    // --- SimpleDialog ---

    @Test
    fun simpleDialog_light() {
        composeTestRule.setContent {
            AppTheme(isDark = false) {
                SimpleDialog(
                    title = "退出しますか？",
                    message = "通話から退出します。",
                    onConfirm = {},
                    onDismiss = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/SimpleDialog_light.png",
        )
    }

    @Test
    fun simpleDialog_dark() {
        composeTestRule.setContent {
            AppTheme(isDark = true) {
                SimpleDialog(
                    title = "退出しますか？",
                    message = "通話から退出します。",
                    onConfirm = {},
                    onDismiss = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/SimpleDialog_dark.png",
        )
    }

    // --- SampleScreen（統合テスト: TopAppBar + PrimaryButton） ---

    @Test
    fun sampleScreen_light() {
        composeTestRule.setContent {
            AppTheme(isDark = false) {
                SampleScreen()
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/SampleScreen_light.png",
        )
    }

    @Test
    fun sampleScreen_dark() {
        composeTestRule.setContent {
            AppTheme(isDark = true) {
                SampleScreen()
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/SampleScreen_dark.png",
        )
    }

    // --- SettingsScreen（TopAppBarのみ使用） ---

    @Test
    fun settingsScreen_light() {
        composeTestRule.setContent {
            AppTheme(isDark = false) {
                SettingsScreen()
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/SettingsScreen_light.png",
        )
    }

    @Test
    fun settingsScreen_dark() {
        composeTestRule.setContent {
            AppTheme(isDark = true) {
                SettingsScreen()
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/SettingsScreen_dark.png",
        )
    }

    // --- LoginScreen（PrimaryButtonのみ使用） ---

    @Test
    fun loginScreen_light() {
        composeTestRule.setContent {
            AppTheme(isDark = false) {
                LoginScreen()
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/LoginScreen_light.png",
        )
    }

    @Test
    fun loginScreen_dark() {
        composeTestRule.setContent {
            AppTheme(isDark = true) {
                LoginScreen()
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/LoginScreen_dark.png",
        )
    }

    // --- ProfileScreen（新規画面: TopAppBar + PrimaryButton） ---

    @Test
    fun profileScreen_light() {
        composeTestRule.setContent {
            AppTheme(isDark = false) {
                ProfileScreen()
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/ProfileScreen_light.png",
        )
    }

    @Test
    fun profileScreen_dark() {
        composeTestRule.setContent {
            AppTheme(isDark = true) {
                ProfileScreen()
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/ProfileScreen_dark.png",
        )
    }
}
