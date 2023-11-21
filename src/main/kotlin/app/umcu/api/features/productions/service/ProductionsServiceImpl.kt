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

package app.umcu.api.features.productions.service

import app.umcu.api.features.productions.model.Production
import app.umcu.api.features.productions.repository.ProductionsRepository
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Service
class ProductionsServiceImpl(val productionsRepository: ProductionsRepository) : ProductionsService {

	override fun findAllProductions(): List<Production> {
		return productionsRepository.findAll(Sort.by(Sort.Direction.ASC, "releaseDate"))
	}

	override fun findProductionBySlug(slug: String): Production? {
		return productionsRepository.findById(slug).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND)
		}
	}

	override fun findNextProduction(dateString: String?): Production? {
		val localDate = if (dateString.isNullOrBlank()) {
			LocalDate.now()
		} else {
			try {
				LocalDate.parse(dateString)
			} catch (e: DateTimeParseException) {
				LocalDate.now()
			}
		}
		return findAllProductions().firstOrNull { it.releaseDate?.isAfter(localDate) ?: false }
	}
}
