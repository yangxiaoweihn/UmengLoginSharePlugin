import 'dart:async';

import 'package:flutter/services.dart';


class PluginUmengLoginShare {

    static const MethodChannel _channelLogin = const MethodChannel('dyt.ws.plugin/login');
    ///plat 登录平台
    /// QQ  WEIXIN  SINA
    static Future<String> doLogin(String plat) async {
        return await _channelLogin.invokeMethod(
            'doLogin',
            {'plat': _platTransfer(plat)},
        );
    }

    static const MethodChannel _channelShare = const MethodChannel('dyt.ws.plugin/share');
    ///plat 登录平台
    ///jsonData 分享数据
    static Future<int> doShare(String plat, String jsonData) async {

        return await _channelShare.invokeMethod(
            'doShare',
            {"plat": _platTransfer(plat), "data": jsonData},
        );
    }

    static String _platTransfer(String plat) {

        //映射到其他平台指定类型
        if (plat.toUpperCase() == 'WEIXIN') {
//            return 'wxsession';
        }

        return plat;
    }
}
