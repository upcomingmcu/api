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

package app.umcu.api.features.productions.model

import app.umcu.api.extensions.toSlug
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.*
import java.time.ZonedDateTime

@Suppress("unused")
@Entity
@Table(name = "productions", uniqueConstraints = [UniqueConstraint(columnNames = ["slug"])])
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Production(
	@Column(nullable = false) var tmdbId: Int? = null,
	@Column(columnDefinition = "TEXT", nullable = false) var title: String? = null,
	@Column(columnDefinition = "TEXT", nullable = true) var overview: String? = null,
	@Column(nullable = true) var releaseDate: ZonedDateTime? = null,
	@Column(nullable = true) var poster: String? = null,
	@Column(nullable = true) @Enumerated(EnumType.STRING) var mediaType: MediaType? = null,
	@Id @Column(nullable = false) val slug: String? = title?.toSlug()
) {
	enum class MediaType {
		MOVIE, TV
	}
}
