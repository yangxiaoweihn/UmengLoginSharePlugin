#import "PluginUmengLoginPlugin.h"
#import <plugin_umeng_login/plugin_umeng_login-Swift.h>

@implementation PluginUmengLoginPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPluginUmengLoginPlugin registerWithRegistrar:registrar];
}
@end
