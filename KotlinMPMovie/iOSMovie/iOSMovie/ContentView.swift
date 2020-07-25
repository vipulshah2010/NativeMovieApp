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
                MovieRow(movie: movie,rating: movie.vote_average/10)
            }
            .navigationBarTitle(Text("Movies"), displayMode: .large)
            .onAppear(perform: {
                self.movieService.fetchMovies()
            })
        }
    }
}

struct MovieRow : View {
    
    var movie: Movie
    @State var rating: Float = 0.0
    
    var body: some View {
        HStack {
            KFImage(URL(string: "http://image.tmdb.org/t/p/w200\(movie.poster_path)")!)
                .resizable()
                .frame(width: 100, height: 150, alignment: .center)
                .cornerRadius(5)
            VStack(alignment: .leading) {
                Spacer().frame(height: 20)
                Text(movie.title).font(.headline)
                    .alignmentGuide(.top) { d in d[.top] }
                Text("\(movie.release_date)").font(.subheadline)
                Spacer()
            }
            Spacer()
            ProgressBar(progress: self.$rating)
                .frame(width: 40.0, height: 50.0,alignment: .trailing)
                .padding(10.0)
        }
    }
    
    func setRating(rating:Float) {
        self.rating = rating
    }
}

struct ContentView_Previews: PreviewProvider {
    
    static var previews: some View {
        
        List {
            MovieRow(movie: Movie(poster_path: "/hPkqY2EMqWUnFEoedukilIUieVG.jpg",
                                  title: "The Outpost",
                                  vote_average: 5.5,
                                  release_date: "2020-06-24",
                                  overview: ""))
        }
    }
}
