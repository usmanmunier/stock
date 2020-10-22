package com.example.stock.authentication;

public class LDAPStrategy implements AuthenticationStrategy {

    @Override
    public boolean authenticateUser() {
        return true;
    }
}
