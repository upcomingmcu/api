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

import app.umcu.api.features.productions.model.Production
import app.umcu.api.features.productions.service.ProductionsService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["/productions", "/p"], method = [RequestMethod.GET])
class ProductionsController(val productionsService: ProductionsService) {

	@GetMapping
	fun findAllProductions(): List<Production> {
		return productionsService.findAllProductions()
	}

	@GetMapping("/{slug}")
	fun findProductionBySlug(@PathVariable slug: String): Production? {
		return productionsService.findProductionBySlug(slug)
	}

	@GetMapping("/next")
	fun findNextProduction(
		@RequestParam(required = false) date: String? = null,
		httpServletResponse: HttpServletResponse
	) {
		val next = productionsService.findNextProduction(date) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
		return httpServletResponse.sendRedirect("/productions/${next.slug}")
	}
}
