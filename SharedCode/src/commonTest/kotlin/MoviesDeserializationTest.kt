import kotlinx.serialization.json.Json
import model.Movie
import model.Movies
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val testJson = Json { ignoreUnknownKeys = true }

class MoviesDeserializationTest {

    @Test
    fun testMovieDeserialization() {
        val raw = """
            {
                "poster_path": "/test.jpg",
                "title": "Test Movie",
                "vote_average": 7.5,
                "release_date": "2024-01-01",
                "overview": "A test overview"
            }
        """.trimIndent()

        val movie = testJson.decodeFromString<Movie>(raw)

        assertEquals("Test Movie", movie.title)
        assertEquals(7.5f, movie.voteAverage)
        assertEquals("/test.jpg", movie.posterPath)
        assertEquals("2024-01-01", movie.releaseDate)
    }

    @Test
    fun testMoviesListDeserialization() {
        val raw = """
            {
                "results": [
                    {
                        "poster_path": "/a.jpg",
                        "title": "Movie A",
                        "vote_average": 8.0,
                        "release_date": "2024-01-01",
                        "overview": "Overview A"
                    },
                    {
                        "poster_path": "/b.jpg",
                        "title": "Movie B",
                        "vote_average": 6.5,
                        "release_date": "2024-02-01",
                        "overview": "Overview B"
                    }
                ]
            }
        """.trimIndent()

        val movies = testJson.decodeFromString<Movies>(raw)

        assertEquals(2, movies.results.size)
        assertEquals("Movie A", movies.results.first().title)
        assertEquals("Movie B", movies.results.last().title)
    }

    @Test
    fun testUnknownFieldsIgnored() {
        val raw = """
            {
                "poster_path": "/test.jpg",
                "title": "Test",
                "vote_average": 7.5,
                "release_date": "2024-01-01",
                "overview": "Overview",
                "unknown_field": "should be ignored",
                "id": 12345
            }
        """.trimIndent()

        val movie = testJson.decodeFromString<Movie>(raw)
        assertNotNull(movie)
        assertEquals("Test", movie.title)
    }
}
