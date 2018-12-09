package by.stepovoy.model;

public enum Role {
    USER("User"), MODER("Moder"), ADMIN("Admin");

    private final String field;

    Role(String role) {
        field = role;
    }

    @Override
    public String toString() {
        return field;
    }
}
