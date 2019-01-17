package es.upm.miw.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;

import java.time.LocalDateTime;
import java.util.Arrays;

@JsonInclude(Include.NON_NULL)
public class UserDto extends UserMinimumDto {

    private String password;

    private String email;

    private String dni;

    private String address;

    private Boolean active;

    private Role[] roles;

    private LocalDateTime registrationDate;

    public UserDto() {
        // Empty for framework
    }

    public UserDto(String mobile, String username, String password, String email, String dni, String address, Boolean active,
                   Role[] roles) {
        super(mobile, username);
        this.setPassword(password);
        this.email = email;
        this.setDni(dni);
        this.address = address;
        this.active = active;
        this.roles = roles;
    }

    public UserDto(String mobileNamePass) {
        this(mobileNamePass, "name" + mobileNamePass, "pass" + mobileNamePass, null, null,
                null, true, new Role[]{Role.CUSTOMER});
    }

    public UserDto(User user) {
        super(user.getMobile(), user.getUsername());
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.dni = user.getDni();
        this.address = user.getAddress();
        this.active = user.isActive();
        this.roles = user.getRoles();
        this.registrationDate = user.getRegistrationDate();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email.toLowerCase();
        } else {
            this.email = null;
        }
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni != null) {
            this.dni = dni.toUpperCase();
        } else {
            this.dni = null;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Role[] getRoles() {
        return this.roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", dni='" + dni + '\'' +
                ", address='" + address + '\'' +
                ", active=" + active +
                ", roles=" + Arrays.toString(roles) +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
