/*
 * Copyright (C) 2015 NS Solutions Corporation
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

package com.htmlhifive.pitalium.sample.popupWindow;

import org.junit.Test;

import com.htmlhifive.pitalium.core.PtlTestBase;

/**
 * ポップアップウィンドウを開いて操作をするテストサンプルクラス
 */
public class PopupWindowTest extends PtlTestBase {

	@Test
	public void popupWindowTest() throws Exception {
		// １．メイン画面を開き、全体のスクリーンショットを撮影します。
		driver.get("popupWindow");
		assertionView.assertView("OpenMainWindow");

		// ２．ボタンをクリックして子画面を開きます。
		driver.findElementById("login").click();

		// ３．WebDriverの対象Windowを子画面に設定します。
		//     switchTo().window()に渡すWindow名は、JavaScriptでwindow.openをコールした時の第二引数です。
		String mainWindow = driver.getWindowHandle();
		driver.switchTo().window("login");

		// ４．移動先のWindowでスクリーンショットを撮影します。
		assertionView.assertView("OpenLoginWindow");

		// ５．ログイン情報を入力してスクリーンショットを撮影します。
		driver.findElementById("email").sendKeys("pitalium.sample@localhost.com");
		driver.findElementById("password").sendKeys("password");
		assertionView.assertView("EnterLoginParams");

		// ６．ログインボタンをクリックし、WebDriverの対象Windowを元の画面へと戻します。
		//     移動後スクリーンショットを撮影します。
		driver.findElementById("login").click();
		driver.switchTo().window(mainWindow);
		assertionView.assertView("LoggedIn");
	}

}
