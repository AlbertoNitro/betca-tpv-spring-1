package es.upm.miw.dtos;

import es.upm.miw.documents.Role;

import java.util.Arrays;

public class TokenOutputDto {

    private String token;

    private String mobile;

    private String name;

    private Role[] roles;

    public TokenOutputDto() {
        // Empty for framework
    }

    public TokenOutputDto(String token, String mobile, String name, Role[] roles) {
        this.token = token;
        this.mobile = mobile;
        this.name = name;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
