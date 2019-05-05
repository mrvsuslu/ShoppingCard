package com.system.shoppingcard.authentication;

import org.springframework.http.HttpHeaders;

public interface ShoppingCardAuthService {

    void checkHeaderAuthToken(HttpHeaders httpHeaders);

}
