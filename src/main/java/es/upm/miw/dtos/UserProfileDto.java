package es.upm.miw.dtos;

import es.upm.miw.documents.User;

public class UserProfileDto {

    private String mobile;
    private String password;

    public UserProfileDto() {}

    public UserProfileDto(User user) {
        this.mobile = user.getMobile();
        this.password = user.getPassword();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserProfileDto{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
