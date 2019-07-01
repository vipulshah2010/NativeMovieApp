//
//  PokeListViewController+UITableView.swift
//  iosApp
//
//  Created by Stefano Venturin on 08/01/2019.
//

import UIKit
import app

extension MovieListViewController: UITableViewDataSource, UITableViewDelegate {
    internal func update(data: [Movie]) {
        movies.removeAll()
        movies.append(contentsOf: data)
        movieTableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return movies.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "movieListCell", for: indexPath)
        let movie = movies[indexPath.row]
        
        cell.textLabel?.text = movie.title
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let posterPath = movies[indexPath.row].poster_path
        loadMoviePoster(posterPath: posterPath)
    }
}
