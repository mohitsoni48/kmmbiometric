//
//  VerifyBiometric.swift
//  iosApp
//
//  Created by Mohit Soni on 14/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct VerifyBiometric: View {
    
    @Binding var path: NavigationPath

    var body: some View {
        VStack {
            Image("biometric")
                .resizable()
                .scaledToFit()
            Spacer()
            Button(action: {
                
            }, label: {
                Text("Veify Biometric")
            })
        }.padding()
    }
}
