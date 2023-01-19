package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/teste")
public class TestController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @GetMapping("/cozinhas/por-nome")
    public List<Cozinha> cozinhasPorNome(@RequestParam("nome") String nome){
        return cozinhaRepository.findTodasByNome(nome);
    }

    @GetMapping("/cozinhas/unica-por-nome")
    public Optional<Cozinha> cozinhaPorNome(String nome){
        return cozinhaRepository.findByNome(nome);
    }

    @GetMapping("/cozinhas/like-por-nome")
    public List<Cozinha> cozinhaPorNomeLike(String nome){
        return cozinhaRepository.findTodasByNomeContaining(nome);
    }

    @GetMapping("/cozinhas/exists")
    public boolean cozinhaExists(String nome){
        return cozinhaRepository.existsByNome(nome);
    }

    @GetMapping("/restaurantes/between-taxa-frete")
    public List<Restaurante> taxaFreteBetween(BigDecimal txInicial, BigDecimal txFinal){
        return restauranteRepository.findByTaxaFreteBetween(txInicial, txFinal);
    }

    @GetMapping("/restaurantes/nome-like-and-cozinhaid")
    public List<Restaurante> nomeLikeAndCozinhaid(String nome, Long id){
        return restauranteRepository.findByNomeContainingAndCozinhaId(nome, id);
    }

    @GetMapping("/restaurantes/find-first-by-nome-containing")
    public Optional<Restaurante> findFirstByNomeContaining(String nome, Long id){
        return restauranteRepository.findFirstByNomeContaining(nome);
    }

    @GetMapping("/restaurantes/find-top2-by-nome-containing")
    public List<Restaurante> findTop2ByNomeContaining(String nome){
        return restauranteRepository.findTop2ByNomeContaining(nome);
    }

    @GetMapping("/restaurantes/count-res")
    public int countByCozinhaId(Long id){
        return restauranteRepository.countByCozinhaId(id);
    }

    @GetMapping("/restaurantes/find-by-name-with-query-annotation")
    public List<Restaurante> consultarPorNome(String nome, Long id){
        return restauranteRepository.consultarPorNome(nome, id);
    }

    @GetMapping("/restaurantes/find-by-name-between-with-impl")
    public List<Restaurante> restaurantesPorNomeFreteComImpl(String nome, BigDecimal txInicial, BigDecimal txFinal){
        return restauranteRepository.find(nome, txInicial, txFinal);
    }
    @GetMapping("/restaurantes/com-frete-gratis")
    public List<Restaurante> restauranteComFreteGratis(String nome){
//        var comFreteGratis = new RestauranteComFreteGratisSpec();
//        var comNomeSemelhante = new RestauranteComNomeSemelhanteSpec(nome);

        //return restauranteRepository.findAll(comFretegratis().and(comNomeSemelhante(nome)));

        return restauranteRepository.findComFreteGratis(nome);
    }

    @GetMapping("/cozinhas/primeiro")
    public Optional<Cozinha> cozinhaPrimeiro(){
        return cozinhaRepository.buscarPrimeiro();
    }

    @GetMapping("/restaurantes/primeiro")
    public Optional<Restaurante> restaurantePrimeiro(){
        return restauranteRepository.buscarPrimeiro();
    }

}
