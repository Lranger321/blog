package main.persistence.entity;

public enum Permission {
USER("user:write"),
    MODERATE("user:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
