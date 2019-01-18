package es.upm.miw.dtos;

import es.upm.miw.documents.Role;

import java.util.Arrays;

public class TokenOutputDto {

    private String token;

    private Role[] roles;

    public TokenOutputDto() {
        // Empty for framework
    }

    public TokenOutputDto(String token, Role[] roles) {
        this.token = token;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "TokenOutputDto{" +
                "token='" + token + '\'' +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
