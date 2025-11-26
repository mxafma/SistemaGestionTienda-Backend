package com.tienda.backend.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio mínimo para validar tokens.
 *
 * Nota: implementación temporal. Reemplazar por JWT u otro mecanismo en producción.
 */
public class TokenService {

    private static final Set<String> TOKENS = ConcurrentHashMap.newKeySet();

    static {
        // Token de ejemplo para pruebas. Cambiar o gestionar dinámicamente.
        TOKENS.add("secrettoken123");
    }

    public static boolean isValid(String token) {
        if (token == null || token.isEmpty()) return false;
        return TOKENS.contains(token);
    }

    public static void addToken(String token) {
        if (token != null && !token.isEmpty()) TOKENS.add(token);
    }

    public static void removeToken(String token) {
        TOKENS.remove(token);
    }
}
