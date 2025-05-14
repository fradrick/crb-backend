package zw.co.rbz.crb_backend.auth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class KeycloakAuthoritiesExtractor implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public KeycloakAuthoritiesExtractor() {
        // Use the new way to set custom authority converter
        this.jwtAuthenticationConverter = new JwtAuthenticationConverter();
        this.jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            return new KeycloakRoleConverter().convert(jwt);
        });
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return jwtAuthenticationConverter.convert(jwt);
    }
}
