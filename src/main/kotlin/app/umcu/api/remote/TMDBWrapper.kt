/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package app.umcu.api.remote

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class TMDBWrapper(
	private var restTemplate: RestTemplate,
	private val apiToken: String = System.getenv()["TMDB_READ_ACCESS_TOKEN"] ?: ""
) {
	private val logger: Logger = LoggerFactory.getLogger(TMDBWrapper::class.java)

	@Suppress("unused")
	enum class PosterSize(val value: String) {
		W92("w92"), W154("w154"), W185("w185"), W342("w342"), W500("w500"), W780("w780"), ORIGINAL("original");
	}

	private inline fun <reified T : Any> get(url: String): T? {
		val headers = HttpHeaders()
		headers.setBearerAuth(apiToken)
		val response: ResponseEntity<T> = restTemplate.exchange(
			url,
			HttpMethod.GET,
			HttpEntity("body", headers),
			object : ParameterizedTypeReference<T>() {})
		return response.body
	}

	private inline fun <reified T : Any> getFromTMDB(endpoint: String): T? {
		val url = "https://api.themoviedb.org/3/$endpoint"
		logger.info("GET \"$url\"")
		return get<T>(url)
	}

	private fun getListDetails(listId: Int, page: Int = 1): TMDBListDetails? {
		return getFromTMDB<TMDBListDetails>("list/$listId?page=$page")
	}

	fun getListItems(listId: Int): ArrayList<TMDBListItem>? {
		val listItems: ArrayList<TMDBListItem> = ArrayList()
		var page = 1
		do {
			val tmdbListDetails = getListDetails(listId, page) ?: return null
			listItems.addAll(tmdbListDetails.items)
			page += 1
		} while (tmdbListDetails.page != tmdbListDetails.totalPages)
		return if (listItems.size <= 0) null else listItems
	}

	fun getMovieDetails(movieId: Int): TMDBMovieDetails? {
		return getFromTMDB<TMDBMovieDetails>("movie/$movieId")
	}

	private fun getMovieReleaseDates(movieId: Int): TMDBMovieReleaseDates? {
		return getFromTMDB<TMDBMovieReleaseDates>("movie/$movieId/release_dates")
	}

	fun getSeriesDetails(seriesId: Int): TMDBTVSeriesDetails? {
		return getFromTMDB<TMDBTVSeriesDetails>("tv/$seriesId")
	}

	fun getReleaseDateByRegion(movieDetails: TMDBMovieDetails, iso31661: String = "US"): String? {
		val movieReleaseDates = getMovieReleaseDates(movieDetails.id) ?: return null
		val releaseDates =
			movieReleaseDates.results.firstOrNull { it.iso31661 == iso31661 }!!.releaseDates.firstOrNull { it.type == ReleaseType.Theatrical }
				?: return null
		return releaseDates.releaseDate
	}

	fun getPosterUrl(posterPath: String?, posterSize: PosterSize = PosterSize.ORIGINAL): String? {
		return if (posterPath.isNullOrEmpty()) null else "https://image.tmdb.org/t/p/${posterSize.value}/$posterPath"
	}
}
