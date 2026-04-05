import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import model.Movie
import model.Movies

class MoviesRepository {

    private val httpClient = createHttpClient()

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3"
        private const val API_KEY = "55957fcf3ba81b137f8fc01ac5a31fb5"
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w200"
    }

    @Throws(Exception::class)
    suspend fun getMovies(): List<Movie> {
        return httpClient
            .get("$BASE_URL/movie/now_playing") {
                parameter("api_key", API_KEY)
            }
            .body<Movies>()
            .results
    }
}
