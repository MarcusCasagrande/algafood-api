package com.algaworks.algafood.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

// 24.16: classe pra testar qual container foi enviada a request
@RestController
public class HostCheckController {

    @GetMapping("/hostcheck")
    public String checkHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress() // retorna o ip do container que ta recebendo a request
            + " - " + InetAddress.getLocalHost().getHostName();
    }
}
