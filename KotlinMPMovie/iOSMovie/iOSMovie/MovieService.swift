//
//  MovieService.swift
//  iOSMovies
//
//  Created by Vipul Shah on 24/07/2020.
//  Copyright © 2020 Vipul. All rights reserved.
//

import Foundation
import SharedCode

class MovieService:ObservableObject {
    
    @Published var movies = [Movie]()
    
    init() {
        
    }
    
    func fetchMovies() {
        MoviesApi().getMovies(success: { (movies) in
            DispatchQueue.main.async {
                self.movies = movies
            }
        }) { (throwable) in
            print("Failure")
        }
    }
}
