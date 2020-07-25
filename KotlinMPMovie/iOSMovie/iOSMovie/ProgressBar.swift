//
//  ProgressBar.swift
//  iOSMovie
//
//  Created by Vipul Shah on 25/07/2020.
//  Copyright © 2020 Vipul. All rights reserved.
//

import SwiftUI

struct ProgressBar: View {
    @Binding var progress: Float
    
    var body: some View {
        ZStack {
            Circle()
                .stroke(lineWidth: 5.0)
                .opacity(0.3)
                .foregroundColor((progress <= 0.5) ? Color.red:Color.green)
            
            Circle()
                .trim(from: 0.0, to: CGFloat(min(self.progress, 1.0)))
                .stroke(style: StrokeStyle(lineWidth: 5.0, lineCap: .round, lineJoin: .round))
                .foregroundColor((progress <= 0.5) ? Color.red:Color.green)
                .rotationEffect(Angle(degrees: 270.0))
                .animation(.linear)
            Text(String(format: "%.0f%%", min(self.progress, 1.0)*100.0))
                .font(.caption)
                .bold()
        }
    }
}


