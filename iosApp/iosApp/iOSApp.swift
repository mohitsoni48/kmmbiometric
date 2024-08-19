import SwiftUI

@main
struct iOSApp: App {
    
    init() {
        Task {
            try! await CipherUtilIosImpl().removePublicKey()
        }
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
