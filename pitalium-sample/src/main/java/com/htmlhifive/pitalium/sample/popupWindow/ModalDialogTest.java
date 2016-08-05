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

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.htmlhifive.pitalium.core.PtlTestBase;

/**
 * モーダルダイアログを開いて操作をするテストサンプルクラス
 */
public class ModalDialogTest extends PtlTestBase {

	@Test
	public void modalDialogTest() throws Exception {
		// １．メイン画面を開き、全体のスクリーンショットを撮影します。
		driver.get("modalDialog");
		assertionView.assertView("OpenMainWindow");

		// ２．ボタンをクリックして子画面を開きます。
		driver.findElementById("login").click();

		// ３．WebDriverの対象Windowを子画面に設定します。
		//     switchTo().window()に渡すWindow名をWindowHandle一覧から探します。
		//     今回は、メイン画面のWindoHandleでは無い値です。
		String mainWindow = driver.getWindowHandle();
		Set<String> windowHandles = driver.getWindowHandles();
		if (windowHandles.size() != 2) {
			fail("Windowが開かれていない、または複数のポップアップが表示されている");
			return;
		}
		for (String windowHandle : windowHandles) {
			if (!windowHandle.equals(mainWindow)) {
				driver.switchTo().window(windowHandle);
			}
		}
		driver.switchTo().activeElement();

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