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
		"__name" : 'com.htmlhifive.pitalium.sample.modalDialog.LoginController',

		/**
		 * ログインformのサブミットイベントハンドラです。 親ウィンドウに対してログイン実行完了を返します。
		 */
		".form submit" : function(context) {
			context.event.preventDefault();

			window.returnValue = true;
			window.close();
		}
	};
	h5.core.controller('body', controller);
})();