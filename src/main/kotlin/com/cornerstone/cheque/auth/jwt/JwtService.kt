package com.cornerstone.cheque.auth.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtService(
    @Value("\${jwt-secret}")
    private val jwtSecret: String
) {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtSecret.encodeToByteArray())
    private val expirationMs: Long = 1000 * 60 * 60 * 24

    fun generateToken(userId: Long): String {
        val now = Date()
        val expiry = Date(now.time + expirationMs)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey)
            .compact()
    }

    fun extractUserId(token: String): Long =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject.toLong()

    fun isTokenValid(token: String): Boolean {
        return try {
            extractUserId(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}
