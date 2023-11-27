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

package app.umcu.api.features.productions.controller

import app.umcu.api.features.error.Error
import app.umcu.api.features.productions.model.Production
import app.umcu.api.features.productions.service.ProductionsService
import com.sletmoe.bucket4k.SuspendingBucket
import io.github.bucket4j.Bandwidth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@Tag(name = "Productions", description = "Browse through all released, upcoming, and announced productions.")
@RestController
@RequestMapping(
	path = ["/productions"], method = [RequestMethod.GET], produces = ["application/json"]
)
@CrossOrigin(origins = ["*"])
class ProductionsController(val productionsService: ProductionsService) {
	private val bucket = SuspendingBucket.build {
		addLimit(Bandwidth.simple(60, 1.minutes.toJavaDuration()))
	}

	@Operation(summary = "Get all released, upcoming, and announced productions.")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200", description = "Productions found.", content = [Content(
				mediaType = "application/json", array = ArraySchema(
					schema = Schema(implementation = Production::class)
				)
			)]
		), ApiResponse(
			responseCode = "429", description = "Rate limit exceeded.", content = [Content(
				mediaType = "application/json", schema = Schema(implementation = Error::class)
			)]
		)]
	)
	@GetMapping
	fun findAllProductions(): List<Production> {
		if (bucket.tryConsume(1)) {
			return productionsService.findAllProductions()
		} else {
			throw ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS)
		}
	}

	@Operation(summary = "Get a specific production.")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200", description = "Production found.", content = [Content(
				mediaType = "application/json", schema = Schema(implementation = Production::class)
			)]
		), ApiResponse(
			responseCode = "404", description = "Production not found.", content = [Content(
				mediaType = "application/json", schema = Schema(implementation = Error::class)
			)]
		), ApiResponse(
			responseCode = "429", description = "Rate limit exceeded.", content = [Content(
				mediaType = "application/json", schema = Schema(implementation = Error::class)
			)]
		)]
	)
	@GetMapping("/{slug}")
	fun findProductionBySlug(
		@Parameter(
			required = true, description = "A production's slug.", example = "iron-man"
		) @PathVariable slug: String
	): Production? {
		if (bucket.tryConsume(1)) {
			return productionsService.findProductionBySlug(slug)
		} else {
			throw ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS)
		}
	}

	@Operation(summary = "Get the next production to be released.")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200", description = "Upcoming production found.", content = [Content(
				mediaType = "application/json", schema = Schema(implementation = Production::class)
			)]
		), ApiResponse(
			responseCode = "404", description = "No further productions.", content = [Content(
				mediaType = "application/json", schema = Schema(implementation = Error::class)
			)]
		), ApiResponse(
			responseCode = "429", description = "Rate limit exceeded.", content = [Content(
				mediaType = "application/json", schema = Schema(implementation = Error::class)
			)]
		)]
	)
	@GetMapping("/next")
	fun findNextProduction(
		@Parameter(
			required = false,
			description = "Find the next production after the date. In the format of \"YYYY-MM-DD\".",
			example = "2008-05-02"
		) @RequestParam(required = false) date: String? = null
	): Production? {
		if (bucket.tryConsume(1)) {
			return productionsService.findNextProduction(date)
		} else {
			throw ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS)
		}
	}
}
