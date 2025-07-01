    package com.example.demo.Security;

    import com.example.demo.entity.User;
    import io.jsonwebtoken.*;
    import io.jsonwebtoken.io.Decoders;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    import java.security.Key;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.function.Function;
    import java.util.stream.Collectors;

    @Component
    public class JwtUtil {

        @Value("${jwt.secret}")
        private String secretKey;
        private static final long JWT_EXPIRATION_MS = 60 * 60 * 1000; // 30 minutes




        public String generateToken(User user) {
            Map<String, Object> claims = new HashMap<>();

            claims.put("Nudoss", user.getNudoss());
            claims.put("userID", user.getUserID());
            claims.put("username", user.getUsername());
            claims.put("firstname", user.getFirstname());
            claims.put("lastname", user.getLastname());
            claims.put("phonenumber", user.getPhonenumber());
            claims.put("email", user.getEmail());
            claims.put("matcle", user.getMatcle());
            claims.put("soccle", user.getSoccle());
            claims.put("language", user.getLanguage());
            claims.put("photo", user.getPhoto());
            // Convert roles to a list of maps
            List<Map<String, Object>> rolesList = user.getRoles().stream().map(role -> {
                Map<String, Object> roleData = new HashMap<>();
                roleData.put("name", role.getName());
                roleData.put("label", role.getLabel());
                roleData.put("category", role.getCategory().name()); // Enum
                roleData.put("model", role.getModel().name());       // Enum
                roleData.put("modelLabel", role.getModelLabel());
                return roleData;
            }).collect(Collectors.toList());

            claims.put("roles", rolesList);

            return createToken(claims, user.getUsername());
        }

        private String createToken(Map<String, Object> claims, String subject) {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
        }


        Claims extractAllClaims(String token) {
            try {
                return Jwts.parserBuilder()
                        .setSigningKey(getSignKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            } catch (Exception e) {
                System.out.println("Failed to extract claims: " + e.getMessage());
                return null;
            }
        }
        public Object extractRoles(String token) {
            return extractClaim(token, claims -> claims.get("roles"));
        }

        public boolean isTokenValid(String token, User user) {
            String username = extractUsername(token);
            return (username.equals(user.getUsername()) && !isTokenExpired(token));
        }
        private Key getSignKey() {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        }


        public boolean validateToken(String token, String username) {
            return username.equals(extractUsername(token)) && !isTokenExpired(token);
        }

        public String extractUsername(String token) {
            try {
                return extractClaim(token, Claims::getSubject);
            } catch (Exception e) {
                System.out.println("Failed to extract username: " + e.getMessage());
                return null;
            }
        }


        private boolean isTokenExpired(String token) {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        }

        private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsResolver.apply(claims);
        }
    }
