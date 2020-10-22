package com.example.stock.authentication;

public enum AuthType {
    LDAP("LDAP"),
    BROWSER("BROWSER");

    private String value;

    AuthType(String _type) {
        this.value = _type;
    }

    public String getValue() {
        return this.value;
    }
}
