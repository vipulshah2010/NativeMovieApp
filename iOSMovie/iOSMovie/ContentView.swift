//
//  ContentView.swift
//  iOSMovie
//
//  Copyright © 2020 Vipul. All rights reserved.
//
//  Licensed under the Apache License, Version 2.0

import SwiftUI
import SharedCode

struct ContentView: View {

    @StateObject private var movieService = MovieService()

    var body: some View {
        NavigationStack {
            Group {
                if movieService.isLoading && movieService.movies.isEmpty {
                    ProgressView("Loading movies…")
                } else if let error = movieService.errorMessage, movieService.movies.isEmpty {
                    ErrorView(message: error, onRetry: movieService.fetchMovies)
                } else {
                    MovieListView(movies: movieService.movies)
                }
            }
            .navigationTitle("Now Playing")
            .task {
                movieService.fetchMovies()
            }
        }
    }
}

struct MovieListView: View {
    let movies: [Movie]

    var body: some View {
        List(movies, id: \.title) { movie in
            MovieRow(movie: movie)
        }
        .listStyle(.plain)
    }
}

struct MovieRow: View {
    let movie: Movie

    private var posterURL: URL? {
        URL(string: "https://image.tmdb.org/t/p/w200\(movie.posterPath)")
    }

    private var rating: Float {
        movie.voteAverage / 10.0
    }

    var body: some View {
        HStack(spacing: 12) {
            AsyncImage(url: posterURL) { phase in
                switch phase {
                case .success(let image):
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                case .failure:
                    Image(systemName: "photo")
                        .foregroundStyle(.secondary)
                default:
                    ProgressView()
                }
            }
            .frame(width: 80, height: 120)
            .clipShape(RoundedRectangle(cornerRadius: 8))

            VStack(alignment: .leading, spacing: 4) {
                Text(movie.title)
                    .font(.headline)
                    .lineLimit(2)
                Text(movie.releaseDate)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                Text(movie.overview)
                    .font(.caption)
                    .foregroundStyle(.secondary)
                    .lineLimit(3)
            }

            Spacer()

            ProgressBar(progress: rating)
                .frame(width: 44, height: 44)
        }
        .padding(.vertical, 4)
    }
}

struct ErrorView: View {
    let message: String
    let onRetry: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle")
                .font(.largeTitle)
                .foregroundStyle(.red)
            Text("Failed to load movies")
                .font(.headline)
            Text(message)
                .font(.subheadline)
                .foregroundStyle(.secondary)
                .multilineTextAlignment(.center)
            Button("Retry", action: onRetry)
                .buttonStyle(.borderedProminent)
        }
        .padding()
    }
}

#Preview("Content View") {
    ContentView()
}

#Preview("Movie Row") {
    MovieRow(
        movie: Movie(
            posterPath: "/hPkqY2EMqWUnFEoedukilIUieVG.jpg",
            title: "The Outpost",
            voteAverage: 7.5,
            releaseDate: "2024-06-24",
            overview: "A small US military unit fights to defend their remote outpost from Taliban forces."
        )
    )
    .padding()
}
