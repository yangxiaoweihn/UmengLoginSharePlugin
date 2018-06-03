package ws.dyt.plugin.umeng.loginshare

import android.content.Context
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry.Registrar
import ws.dyt.plugin.umeng.loginshare.main.ModuleInitReceiver
import ws.dyt.plugin.umeng.loginshare.plugin.LoginPlugin
import ws.dyt.plugin.umeng.loginshare.plugin.SharePlugin

class UmengLoginSharePlugin() : MethodCallHandler {
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar): Unit {

            LoginPlugin.registerWith(registrar)
            SharePlugin.registerWith(registrar)
        }

        fun fuckInit(context: Context): Unit {
            ModuleInitReceiver.fuckInit(context)
        }
    }

    override fun onMethodCall(p0: MethodCall?, p1: Result?) {}
}
