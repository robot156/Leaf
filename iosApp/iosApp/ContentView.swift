import SwiftUI
import LeafApp

/// Compose 화면을 SwiftUI 계층에 끼워 넣는 얇은 래퍼.
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        LeafViewControllerKt.leafViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        // 키보드가 올라올 때 Compose 가 직접 인셋을 처리하도록 SwiftUI 의 회피 동작을 끈다.
        ComposeView()
            .ignoresSafeArea(.keyboard)
    }
}
