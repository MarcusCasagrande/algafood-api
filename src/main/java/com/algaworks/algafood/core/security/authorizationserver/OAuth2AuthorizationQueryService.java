package com.algaworks.algafood.core.security.authorizationserver;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.List;

// 27.20: interface pras consultas de users com alguma permissao de consentimento
public interface OAuth2AuthorizationQueryService {

    List<RegisteredClient> listClientsWithConsent(String principalName);

    List<OAuth2Authorization> listAuthorizations(String princialName, String clientId);
}
