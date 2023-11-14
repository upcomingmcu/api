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

@file:Suppress("unused")

package app.umcu.api.config

import app.umcu.api.models.Production
import app.umcu.api.repositories.ProductionsRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class ProductionsConfig {

	/**
	 * Load sample data into the database.
	 * TODO: To be replaced by actual data from TMDB API
	 */
	@Bean
	fun run(productionsRepository: ProductionsRepository): CommandLineRunner {
		return CommandLineRunner {
			productionsRepository.saveAll(
				listOf(
					Production(
						tmdbId = 1771,
						title = "Captain America: The First Avenger",
						releaseDate = LocalDate.of(2011, 7, 22)
					), Production(
						tmdbId = 1726, title = "Iron Man", releaseDate = LocalDate.of(2008, 4, 30)
					), Production(
						tmdbId = 10195, title = "Thor", releaseDate = LocalDate.of(2011, 4, 21)
					), Production(
						tmdbId = 202555, title = "Daredevil: Born Again"
					), Production(
						tmdbId = 122226, title = "Echo", releaseDate = LocalDate.of(2024, 1, 10)
					), Production(
						tmdbId = 1003596, title = "Avengers: The Kang Dynasty", releaseDate = LocalDate.of(2026, 5, 1)
					), Production(
						tmdbId = 1003598, title = "Avengers: Secret Wars", releaseDate = LocalDate.of(2027, 5, 7)
					)
				)
			)
		}
	}
}
