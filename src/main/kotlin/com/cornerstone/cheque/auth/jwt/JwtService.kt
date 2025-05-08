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

    fun generateToken(username: String): String {
        val now = Date()
        val expiry = Date(now.time + expirationMs)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey)
            .compact()
    }

    fun extractUsername(token: String): String =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject

    fun isTokenValid(token: String): Boolean {
        return try {
            extractUsername(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}
