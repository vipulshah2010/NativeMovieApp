//
//  ContentView.swift
//  iOSMovie
//
//  Created by Vipul Shah on 24/07/2020.
//  Copyright © 2020 Vipul. All rights reserved.
//

import SwiftUI
import SharedCode
import KingfisherSwiftUI

struct ContentView: View {
    
    @ObservedObject var movieService = MovieService()
    
    var body: some View {
        NavigationView {
            List(movieService.movies, id: \.title) { movie in
                MovieView(movie: movie)
            }
            .navigationBarTitle(Text("Movies"), displayMode: .large)
            .onAppear(perform: {
                self.movieService.fetchMovies()
            })
        }
    }
}

struct MovieView : View {
    
    var movie: Movie
    
    var body: some View {
        HStack {
            KFImage(URL(string: "http://image.tmdb.org/t/p/w200\(movie.poster_path)")!)
                .resizable()
                .frame(width: 100, height: 150, alignment: .center)
                .cornerRadius(5)
            VStack(alignment: .leading) {
                Text(movie.title).font(.headline)
                Text("\(movie.release_date)").font(.subheadline)
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    
    static var previews: some View {
        
        List {
            MovieView(movie: Movie(title: "The Outpost",
                                   release_date: "2020-06-24",
                                   poster_path: "/hPkqY2EMqWUnFEoedukilIUieVG.jpg"))
        }
    }
}
