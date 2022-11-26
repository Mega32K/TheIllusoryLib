# TheIllusoryLib
这是一个弱智类型的库  
## 如何引导
1.在工作区主目录创建libs文件夹  
2.配置build.gradle  
`build.gradle内`  
```
dependencies { 
    ...... 
    implementation fileTree(dir: 'libs', includes: ['*jar'])  
}
```  
3.重载Gradle  
##  使用特殊事件  
1.注册  
`使用RunnerBus.EVENT_BUS.regiter(<类>.class);`  
2.静态事件处理器  
使用`@SubscribeRunnerEvent注解`  
###  事件实例
```
@Mod(modid = "mod")
public class MyMod {
    @Mod.EventHandler
    public static void init(FMLPreInitializationEvent event) {
        RunnerBus.EVENT_BUS.register(MyMod.class);
    }
    
    @SubscribeRunnerEvent
    public static void onClientUpdate(OnMainThreadEvent event) {
        if (event.mc.player != null)
            event.mc.player.world.updateEntity(event.mc.player);
    }
}
```
#  注意
本lib需要*MixinBootstrap*模组作为前置
*[MixinBootstrap]https://www.mcmod.cn/class/2364.html*
