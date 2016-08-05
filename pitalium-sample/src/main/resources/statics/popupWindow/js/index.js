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

(function() {
	var controller = {
		"__name" : 'com.htmlhifive.pitalium.sample.popupWindow.IndexController',
		"_loginWindow" : null,

		/**
		 * ログインボタンのクリックイベントハンドラです。 ログインを行うポップアップウィンドウを開きます。
		 */
		"#login click" : function(context) {
			context.event.preventDefault();
			this._loginWindow = document.open('popup.html', 'login', '');
		},

		/**
		 * ポップアップウィンドウから送信されるログイン実行イベントのハンドラです。 ログイン後の処理を行います。
		 */
		"{rootElement} loggedin" : function() {
			this.$find('.header').text('ログインしました！');
			this.$find('#login').hide();
		}
	};
	h5.core.controller('body', controller);
})();