package es.upm.miw.documents;

public enum Role {
    ADMIN(5), MANAGER(4), OPERATOR(3), CUSTOMER(2), AUTHENTICATED(1);

    private int level;

    private Role(int level) {
        this.level = level;
    }

    public String roleName() {
        return "ROLE_" + this.toString();
    }

    public int level() {
        return this.level;
    }

}
