package app.umcu.api.features.productions.repository

import app.umcu.api.features.productions.model.Production
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductionsRepository : JpaRepository<Production, String> {}
