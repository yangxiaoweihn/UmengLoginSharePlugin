package ws.dyt.plugin.umeng.loginshare.plugin;

/**
 * @author yangxiaowie
 * @date 2018/05/27
 *
 * 模块跨平台协议，不可更改
 */
public @interface ResultProtocolForHost {
    int success = 0;
    int cancel = 1;
    //内部错误
    int error = -1;
    //参数错误或者缺失
    int param = -2;
}