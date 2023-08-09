// Import the native module. On web, it will be resolved to ExpoApplist.web.ts
// and on native platforms to ExpoApplist.ts
import ExpoApplistModule from "./ExpoApplistModule";

export function getApps(): any[] {
  return ExpoApplistModule.getApps();
}
