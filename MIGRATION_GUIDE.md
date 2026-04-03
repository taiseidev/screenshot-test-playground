# chatwork_android 移植ガイド

## 検証済み環境

| 項目 | バージョン |
|------|----------|
| Roborazzi | 1.41.0 |
| Robolectric | 4.14.1 |
| GraphicsMode | NATIVE |
| SDK | 35 |
| JDK | 17 |
| Gradle | 8.11.1 |

## 移植手順

### 1. libs.versions.toml に依存追加

```toml
[versions]
roborazzi = "1.41.0"

[libraries]
roborazzi = { module = "io.github.takahirom.roborazzi:roborazzi", version.ref = "roborazzi" }
roborazzi-compose = { module = "io.github.takahirom.roborazzi:roborazzi-compose", version.ref = "roborazzi" }
roborazzi-junit-rule = { module = "io.github.takahirom.roborazzi:roborazzi-junit-rule", version.ref = "roborazzi" }

[plugins]
roborazzi = { id = "io.github.takahirom.roborazzi", version.ref = "roborazzi" }
```

### 2. build-logic/ScreenshotTestPlugin.kt を追加

```kotlin
package com.chatwork.android.shard.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class ScreenshotTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.github.takahirom.roborazzi")

            android {
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                        all { test ->
                            test.jvmArgs("-Xmx2g")
                        }
                    }
                }
            }

            extensions.configure<io.github.takahirom.roborazzi.RoborazziExtension>("roborazzi") {
                outputDir.set(project.file("screenshots"))
            }

            dependencies {
                testImplementation(libs.library("roborazzi"))
                testImplementation(libs.library("roborazzi-compose"))
                testImplementation(libs.library("roborazzi-junit-rule"))
                testImplementation(libs.library("robolectric"))
                testImplementation(libs.library("compose-ui-test-junit4"))
            }
        }
    }
}
```

### 3. build-logic/build.gradle.kts にプラグイン登録

```kotlin
register("screenshotTest") {
    id = "chatwork.primitive.screenshot.test"
    implementationClass = "com.chatwork.android.shard.primitive.ScreenshotTestPlugin"
}
```

build-logic の dependencies に追加:
```kotlin
implementation(libs.plugins.roborazzi.get().toString())
```

### 4. 対象モジュールの build.gradle.kts に適用

```kotlin
plugins {
    id("chatwork.primitive.screenshot.test")
}
```

最初の対象: `:design` モジュール（36個のデザインコンポーネント）

### 5. テストコードを追加

```
design/src/test/java/.../screenshot/DesignComponentScreenshotTest.kt
```

既存の Preview 関数を活用してテストを書く。パターン:
```kotlin
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class DesignComponentScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun containedButton_light() {
        composeTestRule.setContent {
            ChatworkTheme(isDark = false) {
                ContainedButtonShowcase()
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "screenshots/ContainedButton_light.png",
        )
    }
}
```

### 6. ベースライン撮影

```bash
./gradlew :design:recordRoborazziDebug
```

`design/screenshots/` にベースライン画像が生成される。これをGitにコミット。

### 7. /review-ui コマンドを配置

```
.claude/commands/review-ui.md
```

このリポジトリの `.claude/commands/review-ui.md` をコピー。

### 8. GitHub Actions に追加

`.github/workflows/pr_lint_test.yml` に `screenshot-test` ジョブと `ai-review` ジョブを追加。
このリポジトリの `.github/workflows/screenshot_review.yml` を参考に。

## 段階的展開

| Phase | モジュール | テスト数目安 |
|-------|----------|------------|
| Phase 1 | `:design` | 36コンポーネント × 2テーマ = 72テスト |
| Phase 2 | `:feature/ftux`, `:feature/settings` | 画面単位テスト追加 |
| Phase 3 | 残りの `:feature` モジュール | 順次追加 |

## 注意事項

- `screenshots/` ディレクトリはGit管理する（ベースラインとして必要）
- `*_compare.png`, `*_actual.png` は `.gitignore` に追加
- Robolectric Native Graphics Mode は SDK 35 で安定動作を確認済み
- テスト実行にはエミュレータ不要（CIでも動作する）
