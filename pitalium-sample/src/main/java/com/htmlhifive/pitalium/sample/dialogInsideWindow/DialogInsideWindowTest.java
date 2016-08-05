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

package com.htmlhifive.pitalium.sample.dialogInsideWindow;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.htmlhifive.pitalium.core.PtlTestBase;

/**
 * ウィンドウ内ダイアログを開いて操作をするテストサンプルクラス
 */
public class DialogInsideWindowTest extends PtlTestBase {

	@Test
	public void dialogInsideWindowTest() throws Exception {
		// １．メイン画面を開き、全体のスクリーンショットを撮影します。
		driver.get("dialogInsideWindow");
		assertionView.assertView("OpenMainWindow");

		// ２．ボタンをクリックしてモーダルを開きます。
		driver.findElementById("openLoginModal").click();

		// ３．モーダルが表示されるのを待ちます。
		WebDriverWait wait = new WebDriverWait(driver, 30L);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginModal")));

		// ４．モーダルのスクリーンショットを撮影します。
		assertionView.assertView("OpenLoginModal");

		// ５．ログイン情報を入力してスクリーンショットを撮影します。
		driver.findElementById("email").sendKeys("pitalium.sample@localhost.com");
		driver.findElementById("password").sendKeys("password");
		assertionView.assertView("EnterLoginParams");

		// ６．ログインボタンをクリックします。
		driver.findElementById("login").click();

		// ７．モーダルが消えるのを待ち、スクリーンショットを撮影します。
		wait.until((ExpectedConditions.invisibilityOfElementLocated(By.id("loginModal"))));
		assertionView.assertView("LoggedIn");
	}

}
