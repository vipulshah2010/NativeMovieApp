//
//  MovieService.swift
//  iOSMovie
//
//  Copyright © 2020 Vipul. All rights reserved.
//
//  Licensed under the Apache License, Version 2.0

import Foundation
import SharedCode

@MainActor
class MovieService: ObservableObject {

    @Published var movies: [Movie] = []
    @Published var isLoading = false
    @Published var errorMessage: String?

    private let repository = MoviesRepository()

    func fetchMovies() {
        guard !isLoading else { return }
        isLoading = true
        errorMessage = nil

        Task {
            do {
                // suspend fun is exposed as async throws by the Kotlin 2.x KMP framework
                let result = try await repository.getMovies()
                movies = result
            } catch {
                errorMessage = error.localizedDescription
            }
            isLoading = false
        }
    }
}
