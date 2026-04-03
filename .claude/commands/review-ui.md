---
description: "AI駆動UIレビュー: 影響範囲分析 + スクショ差分検出 + デザインレビュー"
---

# /review-ui — AI駆動UIレビュー

UIコード変更に対する影響範囲分析、影響テストの選択的実行、差分画像のデザインレビューを自動実行する。

## Step 1: 変更内容の把握

1. `git diff origin/main --name-only` で変更ファイル一覧を取得
   - origin/mainがない場合は `git diff main --name-only` を試す
2. 変更ファイルの内容を Read で読み取り、何が変わったかを正確に把握する
3. 変更を以下のカテゴリに分類する:
   - **テーマ/トークン変更**: Color.kt, Theme.kt → 広範囲影響の可能性
   - **共通コンポーネント変更**: PrimaryButton.kt, TopAppBar.kt 等 → 使用箇所すべてに影響
   - **画面固有の変更**: SampleScreen.kt 等 → 影響は限定的
   - **新規UI追加**: 新ファイルの追加 → ベースライン撮影が必要

## Step 2: 影響範囲の網羅的な特定

変更されたコードから依存関係を辿り、影響するコンポーネント・画面を**すべて**特定する。

### 手順

1. 変更されたファイル内のpublic関数/クラス/プロパティ名を特定
2. それらの名前をGrepで検索し、直接使用しているファイルを見つける
3. さらにそのファイルを使用しているファイルをGrepで再帰的に辿る
4. 最終的に影響を受けるすべてのコンポーネントと画面をリストアップ

### 判断基準の例

- Color.kt の `PrimaryLight` を変更した場合:
  - → Theme.kt の `LightColorScheme.primary` で使用
  - → `MaterialTheme.colorScheme.primary` を使う PrimaryButton（containerColor）、SimpleDialog（確認ボタン色）に影響
  - → PrimaryButton を使う SampleScreen にも影響
  - → TopAppBar は `surface` 色を使っているため影響**なし**

**重要**: 「影響あり」だけでなく「影響なし」の根拠も示すこと。

## Step 3: テスト選定と実行

### 3-1: ベースライン確認

```bash
ls app/screenshots/*.png 2>/dev/null | head -20
```

ベースラインがない場合は `./gradlew recordRoborazziDebug` で初回撮影し、レポートに「ベースラインを撮影しました」と記載して終了する。

### 3-2: 影響範囲に対応するテストを選定

Step 2の結果から、影響を受けるコンポーネント/画面に対応するスクリーンショットテストを特定する。

テストクラスは `app/src/test/` 配下にある。テストメソッド名とコンポーネント名の対応を確認し、実行すべきテストを選定する。

### 3-3: 選定したテストだけ実行

```bash
./gradlew compareRoborazziDebug --tests "テストクラス名.テストメソッド1" --tests "テストクラス名.テストメソッド2"
```

例: PrimaryButton と SampleScreen が影響範囲の場合:
```bash
./gradlew compareRoborazziDebug \
  --tests "com.example.screenshottest.screenshot.ComponentScreenshotTest.primaryButton_light" \
  --tests "com.example.screenshottest.screenshot.ComponentScreenshotTest.primaryButton_dark" \
  --tests "com.example.screenshottest.screenshot.ComponentScreenshotTest.sampleScreen_light" \
  --tests "com.example.screenshottest.screenshot.ComponentScreenshotTest.sampleScreen_dark"
```

**なぜ全テスト実行ではなく選択実行か**: AIが影響範囲を特定した結果がテスト選定という実際のアクションに直結することで、AIの分析に意味が生まれる。

### 3-4: 差分画像の収集

```bash
find app/build/outputs/roborazzi -name "*_compare.png" 2>/dev/null | sort
```

## Step 4: 差分画像のアップロード（CI環境のみ）

環境変数 `GITHUB_ACTIONS` または `REPO_TOKEN` が存在する場合はCI環境。
`cml publish` で各差分画像をGitHub CDNにアップロードし、URLを記憶する。

```bash
cml publish "画像パス"
```

このコマンドはCDN URLを標準出力に返す。各テスト名とURLの対応をレポートで使用する。

ローカル環境ではアップロードせず、ローカルパスをそのまま記載する。

## Step 5: 差分画像のAIデザインレビュー

差分画像（*_compare.png）をReadツールで読み取り、マルチモーダル分析する。

### 各画像について以下を評価

1. **変更前後の違い**: Reference（左）とNew（右）の具体的な差異
2. **意図との整合性**: Step 1のコード変更の意図と、見た目の変化が一致しているか
3. **デザイン品質**:
   - レイアウトの崩れがないか
   - テキストの可読性（色コントラスト比）
   - ダークテーマでの視認性
   - 余白・間隔の一貫性

### 判定基準

- ✅ **意図した変更**: コード変更の意図通りの見た目の変化。問題なし
- ⚠️ **要確認**: 意図した変更だが、コントラスト・アクセシビリティ等の懸念あり
- ❌ **レグレッション**: 意図しない見た目の変化、またはレイアウト崩れ

## Step 6: テストカバレッジ確認

Step 2で特定した「影響を受けるコンポーネント/画面」のうち、スクリーンショットテストがないものを特定する。
テスト未カバーの画面がある場合、手動確認が必要な旨をレポートに記載する。

## Step 7: レポート出力

以下のフォーマットでレポートを出力する。

CI環境ではPRコメントとして自動投稿される。
ローカル環境ではターミナルに出力される。

```
## 🤖 AI駆動UIレビュー

### 変更概要
（何がどう変わったかを1-2行で）

### 影響範囲分析

#### ⭕ 影響あり
| コンポーネント/画面 | 影響の理由 | テストカバレッジ |
|---|---|---|
| PrimaryButton | primary色を直接使用 | ✅ スクショテストあり |

#### ➖ 影響なし（確認済み）
| コンポーネント/画面 | 影響なしの根拠 |
|---|---|
| TopAppBar | surface色を使用、primary色は不使用 |

### 実行したテスト
以下のテストを影響範囲に基づいて選択的に実行しました:
- primaryButton_light / primaryButton_dark
- sampleScreen_light / sampleScreen_dark
（選定理由: PrimaryLight変更 → PrimaryButton, SampleScreenが影響を受けるため）

### スクリーンショット差分

#### {テスト名}
**判定**: ✅ / ⚠️ / ❌
**分析**: （変更前後の違い、デザイン観点の評価を具体的に）

![テスト名](CDN_URLまたはローカルパス)

### 手動確認が必要な画面
テスト未カバーで影響を受ける画面のリスト。
すべてカバーされている場合は「すべての影響画面がテストでカバーされています」と記載。

### 総合判定
変更全体の安全性とマージ可否の推奨を1-3行で記載。
ベースライン更新が必要な場合は `./gradlew recordRoborazziDebug` の実行を案内。
```
