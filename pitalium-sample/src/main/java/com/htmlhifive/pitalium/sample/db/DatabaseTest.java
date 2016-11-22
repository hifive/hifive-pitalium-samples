/*
 * Copyright (C) 2015-2016 NS Solutions Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.htmlhifive.pitalium.sample.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.htmlhifive.pitalium.core.PtlTestBase;
import com.htmlhifive.pitalium.core.selenium.PtlWebDriverWait;

public class DatabaseTest extends PtlTestBase {

	@Before
	@Override
	public void setUp() {
		super.setUp();

		// Capabilitiesに設定した接続先を取得する
		String connectionString = (String) capabilities.getCapability("pitalium.db.connectionString");

		// データベースの初期化を実行する
		try {
			Class.forName("org.h2.Driver");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try (InputStream in = getClass().getResourceAsStream("init_data.xlsx");
				Connection connection = DriverManager.getConnection(connectionString)) {
			// DataSetの生成
			IDataSet dataSet = new XlsDataSet(in);
			IDatabaseConnection databaseConnection = new DatabaseConnection(connection);

			// 高速化用、バッチ処理設定
			databaseConnection.getConnection().setAutoCommit(false);
			databaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);
			databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_BATCH_SIZE, 1000);
			databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_FETCH_SIZE, 1000);

			// データを全て削除し、挿入しなおす
			DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
			databaseConnection.getConnection().commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void deleteUser() throws Exception {
		// アクセス先のURLをCapabilitiesから取得する
		String baseUrl = (String) capabilities.getCapability("pitalium.baseUrl");
		driver.get(baseUrl + "db/user/");

		// 初期表示スクリーンショットの撮影
		assertionView.assertView("0.init");

		// 削除するユーザーを選択し、削除ボタンをクリックする
		List<WebElement> elements = driver.findElementsByClassName("user-check");
		for (int i = 0; i < 2; i++) {
			WebElement element = elements.get(i);
			if (!element.isSelected()) {
				element.click();
			}
		}
		final WebElement deleteButton = driver.findElementById("deleteUser");
		deleteButton.click();

		// 削除はAJAXで実行されるため、AJAXの終了まで待機する
		// 削除後にスクリーンショットを撮影する
		new PtlWebDriverWait(driver, 5).until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
				return "Deleted".equals(deleteButton.getText());
			}
		});
		assertionView.assertView("1.deleted");

		// データベースの値をチェックする
		String connectionString = (String) capabilities.getCapability("pitalium.db.connectionString");
		try (InputStream in = getClass().getResourceAsStream("result_data.xlsx");
				Connection connection = DriverManager.getConnection(connectionString)) {
			// 正解データが含まれるエクセルファイルからDataSetを生成
			IDataSet expectedSet = new XlsDataSet(in);
			IDatabaseConnection databaseConnection = new DatabaseConnection(connection);
			// データベースから比較元のDataSetを生成
			IDataSet actualSet = databaseConnection.createDataSet();

			// 全テーブルの値をまとめてチェックする
			Assertion.assertEquals(expectedSet, actualSet);
		}
	}
}
