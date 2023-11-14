package app.umcu.api.repositories

import app.umcu.api.models.Production
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProductionsService(val productionsRepository: ProductionsRepository) {

	fun findAllProductions(): MutableList<Production> {
		return productionsRepository.findAll(Sort.by(Sort.Direction.ASC, "releaseDate"))
	}

	fun findProductionBySlug(slug: String): Production? {
		return productionsRepository.findById(slug).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND)
		}
	}
}
