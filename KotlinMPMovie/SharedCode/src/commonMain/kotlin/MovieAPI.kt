import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import model.Movie
import model.Movies
import shared.ApplicationDispatcher
import shared.Image
import shared.toNativeImage

class MoviesApi {

    private val httpClient = HttpClient()

    private val url =
        "https://api.themoviedb.org/3/movie/now_playing?api_key=55957fcf3ba81b137f8fc01ac5a31fb5"

    fun getMovies(success: (List<Movie>) -> Unit, failure: (Throwable?) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
            try {
                val json = httpClient.get<String>(url)
                Json {
                    ignoreUnknownKeys = true
                }.decodeFromString(
                    Movies.serializer(),
                    json
                ).results.also(success)
            } catch (ex: Exception) {
                failure(ex)
            }
        }
    }

    fun getMoviePoster(
        posterPath: String, success: (Image?) -> Unit,
        failure: (Throwable?) -> Unit
    ) {
        GlobalScope.launch(ApplicationDispatcher) {
            try {
                val url = "https://image.tmdb.org/t/p/w780$posterPath"
                httpClient.get<ByteArray>(url)
                    .toNativeImage()
                    .also(success)
            } catch (ex: Exception) {
                failure(ex)
            }
        }
    }
}