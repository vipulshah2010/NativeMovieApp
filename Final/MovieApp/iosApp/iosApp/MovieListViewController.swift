import UIKit
import app

class MovieListViewController: UIViewController {
    
    @IBOutlet weak var moviesImageView: UIImageView!
    @IBOutlet weak var movieTableView: UITableView!
    
    internal var movies: [Movie] = []
    
    internal var api = MoviesApi()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        movieTableView.dataSource = self
        movieTableView.delegate = self
        loadMovies()
    }
    
    private func loadMovies(){
        api.getMovies(
            success: { data in
                self.update(data: data)
                return KotlinUnit()
        }, failure: {
            self.handleError($0?.message)
            return KotlinUnit()
        })
    }
    
    internal func loadMoviePoster(posterPath: String){
        self.api.getMoviePoster(posterPath: posterPath, success: { (image) -> KotlinUnit in
            self.moviesImageView.image = image
            return KotlinUnit()
        }, failure: { (throwable) -> KotlinUnit in
            print(throwable?.message ?? "Empty error")
            return KotlinUnit()
        })
    }
    
    internal func handleError(_ error: String?){
        let message = error ?? "An unknown error occurred. Retry?"
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "Retry", style: .default, handler: { action in
            self.loadMovies()
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        
        self.present(alert, animated: true)
    }
}
