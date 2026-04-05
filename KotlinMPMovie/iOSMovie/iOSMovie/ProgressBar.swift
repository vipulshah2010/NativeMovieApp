//
//  ProgressBar.swift
//  iOSMovie
//
//  Copyright © 2020 Vipul. All rights reserved.
//
//  Licensed under the Apache License, Version 2.0

import SwiftUI

struct ProgressBar: View {

    let progress: Float

    private var color: Color {
        progress >= 0.5 ? .green : .red
    }

    var body: some View {
        ZStack {
            Circle()
                .stroke(lineWidth: 5.0)
                .opacity(0.3)
                .foregroundStyle(color)

            Circle()
                .trim(from: 0.0, to: CGFloat(min(progress, 1.0)))
                .stroke(style: StrokeStyle(lineWidth: 5.0, lineCap: .round, lineJoin: .round))
                .foregroundStyle(color)
                .rotationEffect(.degrees(270.0))
                .animation(.linear, value: progress)

            Text(String(format: "%.0f%%", min(progress, 1.0) * 100.0))
                .font(.caption)
                .bold()
        }
    }
}

#Preview {
    HStack {
        ProgressBar(progress: 0.35)
            .frame(width: 60, height: 60)
        ProgressBar(progress: 0.75)
            .frame(width: 60, height: 60)
    }
    .padding()
}
