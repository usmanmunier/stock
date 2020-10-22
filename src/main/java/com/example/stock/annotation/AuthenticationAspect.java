package com.example.stock.annotation;

import com.example.stock.authentication.AuthType;
import com.example.stock.authentication.AuthenticationStrategy;
import com.example.stock.authentication.BrowerAuthStrategy;
import com.example.stock.authentication.LDAPStrategy;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthenticationAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);

    @Autowired
    HttpServletRequest context;

    @Value("${auth.type}")
    private String authType;


    @Before("@annotation(Authenticate)")
    public boolean AuthenticateUser () {
        logger.info("Authenticating user!!");
        AuthenticationStrategy authenticationStrategy = null;
        if (authType.equalsIgnoreCase(AuthType.LDAP.getValue())) {
            authenticationStrategy = new LDAPStrategy();
        } else {
            authenticationStrategy = new BrowerAuthStrategy();
        }

        authenticationStrategy.authenticateUser();

        return true;
    }
}
