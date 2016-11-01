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

package com.htmlhifive.pitalium.sample.three_d;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.htmlhifive.pitalium.core.PtlTestBase;

/**
 * 3Dのウェブページを非同期スクリプトを使用してテストするサンプル
 */
public class ThreeDimensionsTest extends PtlTestBase {

	private static String loadJS(String fileName) throws IOException {
		try (InputStream in = ThreeDimensionsTest.class.getResourceAsStream(fileName)) {
			return IOUtils.toString(in, StandardCharsets.UTF_8);
		}
	}

	@Test
	public void オブジェクトの色を変更() throws Exception {
		// ページの読み込み
		driver.get("three_d");

		// テストスクリプトを実行
		driver.executeAsyncJavaScript(loadJS("オブジェクトの色を変更.js"));

		// スクリーンショットの撮影
		assertionView.assertView("1_変更後");
	}

	@Test
	public void カメラの位置を変更() throws Exception {
		// ページの読み込み
		driver.get("three_d");

		// テストスクリプトを実行
		driver.executeAsyncJavaScript(loadJS("カメラの位置を変更.js"));

		// スクリーンショットの撮影
		assertionView.assertView("1_変更後");
	}

}
