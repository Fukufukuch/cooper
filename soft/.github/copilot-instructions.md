<!-- .github/copilot-instructions.md -->
# Copilot / AI エージェント向け短いガイド

目的: このリポジトリは軽量なJava Servletウェブアプリケーションです。以下は、AIベースのコーディング支援が迅速に実務に入るための要点です。

- プロジェクトの構成:
  - ソース: src/main/java/ (例: src/main/java/HelloServlet.java)
  - ビルド出力: bin/（README.md に準拠）
  - 依存ライブラリ: lib/（Servlet APIなどのjarを置く）
  - Web ルート / エントリ: index.jsp（ルート）
  - デプロイ設定: WEB-INF/web.xml

- アーキテクチャ (ビッグピクチャ):
  - 単純なサーブレットベースのWebアプリ。HTTPリクエストは`@WebServlet`注釈または`WEB-INF/web.xml`でマッピングされる。
  - 静的/ビュー: ルートにあるJSPや webapp フォルダに配置されたリソースを返す想定。
  - 実行環境: 標準的なServletコンテナ（例: Apache Tomcat）にデプロイして動作確認する。

- 重要なプロジェクト固有の慣習/観察点:
  - `HelloServlet.java` はパッケージ宣言が無い（デフォルトパッケージ）。そのためコンパイル後の `.class` は `WEB-INF/classes/` のルートに置く必要がある。
  - 依存は `lib/` に置かれている想定。ローカルビルド時はこれらをクラスパスに含める。
  - Servlet API をコンテナに含めている場合、`WEB-INF/lib` に同一のAPIを入れないよう注意（コンテナとの競合を避ける）。

- ビルド / デプロイ（発見可能なワークフロー）:
  - 手順の概略:
    1. `javac` で `src/main/java` を `lib/*` をクラスパスにしてコンパイルし、出力先を `WEB-INF/classes` にする。
    2. 生成した `WEB-INF/classes` と `index.jsp`, `WEB-INF/web.xml`, 必要な `lib/*.jar` を含めてWARにするか、Tomcat の webapps に配置する。
  - Windows上の例（PowerShell / cmd で実行）:
```bash
mkdir -p WEB-INF/classes
javac -cp "lib/*" -d WEB-INF/classes src/main/java/*.java
rem 次に WEB-INF と index.jsp を含む構成をTomcatのwebapps/<app> に配置
```

- コードパターンの具体例:
  - 注釈マッピング: `@WebServlet("/hello")`（参照: src/main/java/HelloServlet.java）
  - `doGet(HttpServletRequest, HttpServletResponse)` をオーバーライドしてレスポンスを直接書くパターン

- 触るときのチェックポイント（変更による影響を最小に）:
  - `WEB-INF/web.xml` と `@WebServlet` の双方が存在する場合、どちらが優先されるか（環境依存）を意識する。
  - `lib/` のjarを編集・更新するとコンテナのクラスローディングに影響するため、差分は小さくする。

- テスト・デバッグの実務ヒント:
  - 単体テストフレームワークは見当たらない。静的変更はコンパイルしてTomcatにデプロイして確認するワークフローが基本。
  - ローカルでの素早い検証には、`javac`→`WEB-INF/classes`配置→Tomcat再起動/再デプロイを使う。

- 参考ファイル:
  - `index.jsp`（ルートのエントリ）
  - `WEB-INF/web.xml`（デプロイ記述）
  - `src/main/java/HelloServlet.java`（典型的なServlet実装、注釈で `/hello` をマッピング）

フィードバックください: 不明点や追加したいワークフロー（Gradle/Maven化、CI 配備など）があれば教えてください。
