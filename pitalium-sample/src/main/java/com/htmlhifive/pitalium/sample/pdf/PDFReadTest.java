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

package com.htmlhifive.pitalium.sample.pdf;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.Assert;
import org.junit.Test;

import com.htmlhifive.pitalium.core.config.ExecMode;
import com.htmlhifive.pitalium.core.config.PtlTestConfig;
import com.htmlhifive.pitalium.image.model.DiffPoints;
import com.htmlhifive.pitalium.image.util.ImageUtils;

/**
 * PitalimでPDFを比較するサンプル。 <br/>
 * PDFの変換にはApache PDFBox(https://pdfbox.apache.org/) を使用している。
 */
public class PDFReadTest {

	private static final String EXPORT_FILE_PATH = "./exports/pdf/";

	/**
	 * ローカルのPDFファイルを読み込み、画像として保存する。<br />
	 * その後、画像比較で差分を確認する。
	 *
	 * @throws IOException
	 */
	@Test
	public void testCreateImageFromPDF() throws IOException {

		// PDFを画像として保存する
		if (PtlTestConfig.getInstance().getEnvironment().getExecMode() == ExecMode.SET_EXPECTED) {
			savePdfAsImages("sample_expected");
			return;
		}

		int numberOfPages = savePdfAsImages("sample_actual");
		if (numberOfPages < 0) {
			Assert.fail("PDFの変換に失敗しました。");
		}

		// Pitaliumで画像比較
		// 期待結果（sample_expected.pdf）と、今回結果（sample_actual.pdf）を比較する
		for (int i = 0; i < numberOfPages; i++) {
			BufferedImage leftImage = readImage(EXPORT_FILE_PATH + "sample_expected" + i + ".png");
			BufferedImage rightImage = readImage(EXPORT_FILE_PATH + "sample_actual" + i + ".png");
			if (leftImage == null || rightImage == null) {
				Assert.fail("画像が見つかりません。");
			}

			DiffPoints diffPoints = ImageUtils.compare(leftImage, null, rightImage, null, null);

			// 差分があれば差分画像を出力
			if (diffPoints.isFailed()) {
				final BufferedImage diffImage = ImageUtils.getDiffImage(leftImage, rightImage, diffPoints);
				saveExportImage(diffImage, "diffImage.png");
				Assert.fail("PDFが一致しません。");
			}
		}

	}

	/**
	 * PDFを読み込み、画像として保存する。<br/>
	 * PDFのページ1枚につき画像1枚に変換される。<br/>
	 * PDF→画像への変換にはApache PDFBoxを利用する。
	 *
	 * @param fileName 読み込むPDFのファイル名（.pdfは除く）
	 * @return 保存した画像の枚数（PDFのページ数）
	 */
	private int savePdfAsImages(String fileName) {
		BufferedInputStream fileToParse = new BufferedInputStream(getClass().getResourceAsStream(fileName + ".pdf"));
		int numberOfPages = 0;

		try (PDDocument pdf = PDDocument.load(fileToParse)) {
			// 1ページ=1画像として全ページを保存
			final PDFRenderer pdfRenderer = new PDFRenderer(pdf);
			numberOfPages = pdf.getNumberOfPages();

			for (int i = 0; i < numberOfPages; i++) {
				final BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
				if (!saveExportImage(image, fileName + i + ".png")) {
					return -1;
				}
			}
		} catch (IOException e) {
			return -1;
		}
		return numberOfPages;
	}

	/**
	 * EXPORT_PATH以下に画像を保存する。
	 *
	 * @param image 保存する画像
	 * @param fileName 保存するファイル名
	 * @return 正しく保存できればtrue、そうでない場合はfalse
	 */
	private boolean saveExportImage(BufferedImage image, String fileName) {
		File exportDir = new File(EXPORT_FILE_PATH);
		if (!exportDir.exists()) {
			exportDir.mkdirs();
		}

		try {
			return ImageIO.write(image, "png", new File(EXPORT_FILE_PATH + fileName));
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 指定された画像ファイルを読み込む。
	 *
	 * @param filePath 画像ファイルのパス
	 * @return 読み込んだ画像のBufferedImage
	 */
	private BufferedImage readImage(String filePath) {
		File image = new File(filePath);
		if (!image.exists()) {
			return null;
		}

		try {
			return ImageIO.read(image);
		} catch (IOException e) {
			return null;
		}
	}
}
