import SwiftUI
import LeafApp

@main
struct iOSApp: App {
    init() {
        // isDebug 는 Kotlin 쪽에서 알 수 없다. Xcode 의 빌드 구성이 정답이므로 여기서 넘긴다.
        #if DEBUG
        IosAppGraphKt.createIosAppGraph(isDebug: true)
        #else
        IosAppGraphKt.createIosAppGraph(isDebug: false)
        #endif
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea(.all)
        }
    }
}
