// Seleniumにasync実行の完了を通知するコールバック
var callback = arguments[0];
var $body = $('body');


// レンダリング開始前のイベントを受け取って処理をするコールバック
// テスト操作を行う
var preRender = function () {
  $body
    .off('preRender', preRender)
    .on('postRender', postRender);

  // 表示しているオブジェクトの色を緑から赤へ変更する
  var controller = h5.core.controllerManager.getAllControllers()[0];
  controller.changeObjectColor(0xff0000);
};


// レンダリング完了のイベントを受け取って処理をするコールバック
// Seleniumへのコールバック通知を行う
var postRender = function () {
  $body.off('postRender', postRender);
  callback();
};


// コールバックの登録
$body.on('preRender', preRender);