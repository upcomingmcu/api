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
