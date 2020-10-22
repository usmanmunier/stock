package com.example.stock.authentication;

public class BrowerAuthStrategy implements AuthenticationStrategy {
    @Override
    public boolean authenticateUser() {
        return true;
    }
}
