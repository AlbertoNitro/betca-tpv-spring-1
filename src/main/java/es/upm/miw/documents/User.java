package es.upm.miw.documents;

import es.upm.miw.dtos.UserProfileDto;
import es.upm.miw.dtos.UserRolesDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Document
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String mobile;

    private LocalDateTime registrationDate;

    private String username;

    private String password;

    private Boolean active;

    private String email;

    private String dni;

    private String discount;

    private String address;

    private Role[] roles;

    public User() {
        this.registrationDate = LocalDateTime.now();
        this.active = true;
    }

    public User(String mobile, String username, String password, String dni, String discount, String address, String email) {
        this();
        this.mobile = mobile;
        this.username = username;
        this.dni = dni;
        this.discount = discount;
        this.address = address;
        this.email = email;
        this.setPassword(password);
        this.roles = new Role[]{Role.CUSTOMER};
    }

    public User(String mobile, String username, String password) {
        this(mobile, username, password, null, null, null, null);
    }

    public User(String id,String username,String dni,String discount,String email, String address,String password,UserRolesDto userRolesDto) {
        this.id=id;
        this.mobile=userRolesDto.getMobile();
        this.roles=userRolesDto.getRoles();
        this.setUsername(username);
        this.setDni(dni);
        this.setDiscount(discount);
        this.setEmail(email);
        this.setAddress(address);
        this.setPassword(password);


    }
    public User(String id, String username, String dni, String discount, String email, String address, Role[] roles, UserProfileDto userProfileDto) {
        this.id=id;
        this.mobile=userProfileDto.getMobile();
        this.password=userProfileDto.getPassword();
        this.setPassword(this.password);
        this.setUsername(username);
        this.setDni(dni);
        this.setDiscount(discount);
        this.setEmail(email);
        this.setAddress(address);
        this.setRoles(roles);

    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null) {
            this.password = UUID.randomUUID().toString();
        } else {
            this.password = new BCryptPasswordEncoder().encode(password);
        }
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDiscount() { return discount; }

    public void setDiscount(String discount) { this.discount = discount; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        return this.mobile.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && mobile.equals(((User) obj).mobile);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", registrationDate=" + registrationDate +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", email='" + email + '\'' +
                ", dni='" + dni + '\'' +
                ", discount='" + discount + '\'' +
                ", address='" + address + '\'' +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }



    public static class Builder {
        private User user;

        private Builder() {
            this.user = new User();
        }

        public Builder id(String id) {
            this.user.id = id;
            return this;
        }

        public Builder mobile(String mobile) {
            this.user.mobile = mobile;
            return this;
        }

        public Builder username(String username) {
            this.user.username = username;
            return this;
        }

        public Builder password(String password) {
            if (password == null) {
                this.user.password = UUID.randomUUID().toString();
            } else {
                this.user.password = new BCryptPasswordEncoder().encode(password);
            }
            return this;
        }

        public Builder active(Boolean active) {
            this.user.active = active;
            return this;
        }

        public Builder email(String email) {
            this.user.email = email;
            return this;
        }

        public Builder dni(String dni) {
            this.user.dni = dni;
            return this;
        }

        public Builder discount(String discount){
            this.user.discount = discount;
            return this;
        }

        public Builder address(String address) {
            this.user.address = address;
            return this;
        }

        public Builder registrationDate(LocalDateTime registrationDate) {
            this.user.registrationDate = registrationDate;
            return this;
        }

        public Builder roles(Role[] roles) {
            this.user.roles = roles;
            return this;
        }

        public User build() {
            return this.user;
        }
    }


}
