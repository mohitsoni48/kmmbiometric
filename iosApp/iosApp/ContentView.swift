import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greet()
    @State private var path = NavigationPath()

	var body: some View {
        NavigationStack(path: $path) {
            SetPublicKey(path: $path)
        }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
