package expo.modules.applist

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoApplistModule : Module() {
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ExpoApplist')` in JavaScript.
    Name("ExpoApplist")

    // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
    Function("getApps") { apiLevel: Int ->
      val list: WritableArray = Arguments.createArray();

      try {
        val pm: PackageManager = appContext.reactContext!!.getPackageManager();
        val packagesAll: List<PackageInfo>?

        if (apiLevel < 33) {
          packagesAll = pm?.getInstalledPackages(0)
        } else {
          packagesAll = pm?.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
        }

        when (packagesAll) {
          null -> {}
          else ->
            for (packageInfo in packagesAll) {
              val appInfo: WritableMap = Arguments.createMap();

              appInfo.putString("packageName", packageInfo.packageName);
              appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime).toDouble());
              appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime).toDouble());
              appInfo.putString("appName", packageInfo.applicationInfo.loadLabel(pm).toString().trim());
              appInfo.putBoolean("isSystemApp", (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 1);
              //val isSystemAppFlag = packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM
              //when (isSystemAppFlag) {
              //  0 -> appInfo.putBoolean("isSystemApp", false)
              //  else -> appInfo.putBoolean("isSystemApp", true)
              //}
              //appInfo.putInt("flags", packageInfo.applicationInfo.flags)

              list.pushMap(appInfo);
            }
        }
        return@Function list
      } catch (e: Exception) {
        return@Function list
      }
    }
  }
}
