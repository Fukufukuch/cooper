<!-- .github/copilot-instructions.md -->
# Copilot / AI エージェント向け短いガイド

目的: このリポジトリは軽量なJava Servletウェブアプリケーションです。以下は、AIベースのコーディング支援が迅速に実務に入るための要点です。

- プロジェクトの構成:
  - ソース: src/main/java/jp/ac/kochi/tech/ (例: src/main/java/jp/ac/kochi/tech/HelloServlet.java)
  - Web リソース: src/main/webapp/ (JSPファイル、CSS、WEB-INF/web.xml)
  - ビルド出力: target/classes/ (コンパイル済みクラス)、target/soft-1.0-SNAPSHOT/ (WARファイル)
  - 依存ライブラリ: Maven依存 (pom.xml参照)、ローカルlib/ (追加jar)
  - エントリ: index.jsp (ルート)

- アーキテクチャ (ビッグピクチャ):
  - Jakarta EE 6.0ベースのServlet Webアプリ。HTTPリクエストは@WebServletアノテーションでマッピング (web.xmlはフィルタのみ)。
  - データアクセス: DAOパターン (例: ShiftDAO.java)、DBUtil.javaでMySQL接続。
  - ビュー: JSPファイルでレスポンス生成、JSTL使用可能。
  - セキュリティ: AuthLoginModule.javaで認証、セッション管理。
  - 実行環境: Servletコンテナ (Tomcat)にWARデプロイ。

- 重要なプロジェクト固有の慣習/観察点:
  - パッケージ: jp.ac.kochi.tech (全クラス)。
  - データベース: MySQL (shift_db)、ハードコードクレデンシャル (DBUtil.java - 本番では環境変数化推奨)。
  - エンコーディング: UTF-8フィルタ適用 (web.xml)。
  - ビルド: Maven優先 (mvn compile/package)、Java 25ターゲット (プレビュー版?)。
  - ローカルビルド: javac使用時、lib/*をクラスパスに、出力WEB-INF/classes。

- ビルド / デプロイ（発見可能なワークフロー）:
  - Mavenビルド: `mvn compile` (クラス生成)、`mvn package` (WAR作成)。
  - 手順の概略 (ローカル):
    1. `mvn compile` で target/classes にコンパイル。
    2. WARをTomcat webapps に配置、または target/soft-1.0-SNAPSHOT をコピー。
  - Windows例: PowerShellで `mvn compile; mvn package`。

- コードパターンの具体例:
  - Servlet: `@WebServlet("/path")`、doGet/doPostオーバーライド (参照: HelloServlet.java)。
  - DAO: DBUtil.getConnection()使用、PreparedStatementでクエリ (参照: ShiftDAO.java)。
  - JSP: <%= %> スクリプトレット、JSTLタグ (例: ShiftEditList.jsp)。

- 触るときのチェックポイント（変更による影響を最小に）:
  - web.xml変更: フィルタ順序に注意。
  - DB変更: スキーマ一致確認 (user, shiftテーブル)。
  - 依存追加: pom.xml更新、scope=provided注意 (Servlet API)。

- テスト・デバッグの実務ヒント:
  - 単体テストなし: コンパイル後Tomcatデプロイで検証。
  - ローカル検証: mvn compile → WAR配置 → Tomcat再起動。

- 参考ファイル:
  - pom.xml (Maven設定、依存)
  - src/main/webapp/WEB-INF/web.xml (フィルタ設定)
  - src/main/java/jp/ac/kochi/tech/DBUtil.java (DB接続)
  - src/main/java/jp/ac/kochi/tech/ShiftDAO.java (DAO例)

フィードバックください: 不明点や追加したいワークフロー（DBマイグレーション、CI 配備など）があれば教えてください。
