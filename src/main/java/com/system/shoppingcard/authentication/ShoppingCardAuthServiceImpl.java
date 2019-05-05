package com.system.shoppingcard.authentication;

import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingCardAuthServiceImpl implements ShoppingCardAuthService {

    private final LocaleMessageProvider localeMessageProvider;

    public void checkHeaderAuthToken(HttpHeaders httpHeaders) {

        if (httpHeaders == null || !httpHeaders.get(HttpHeaders.WWW_AUTHENTICATE).contains(localeMessageProvider.getMessage("authentication.token")))
            throw new UnsupportedOperationException("error.unauthorized");
    }

}
