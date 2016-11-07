// Seleniumに非同期実行の完了を通知するコールバック
var callback = arguments[arguments.length - 1];

// 待機時間（ミリ秒）
var waitMillis = arguments.length > 2 ? arguments[0] : 0;

// 指定のミリ秒後にrequestAnimationFrameを呼び出すコールバックを実行する
setTimeout(function () {
  requestAnimationFrame(waitFirstRAF);
}, waitMillis);

/**
 * 指定ミリ秒待機した後の最初のrequestAnimationFrameで呼ばれる関数。
 * このrAFでWebGLの描画が更新される。
 */
function waitFirstRAF() {
  requestAnimationFrame(waitSecondRAF);
}

/**
 * 指定ミリ秒待機した後の二回目のrequestAnimationFrameで呼ばれる関数。
 * WebGLの描画が確実に更新されている。
 */
function waitSecondRAF() {
  callback();
}