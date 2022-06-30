package com.nttdata.client.enterprise.controller;

import com.nttdata.client.enterprise.service.impl.EnterpriseClientServiceImpl;
import com.nttdata.client.enterprise.dto.*;
import com.nttdata.client.enterprise.entity.EnterpriseClient;
import com.nttdata.client.general.entity.ClientProfiles;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("client/enterprise")
// @Tag(name = "Enterprise Clients Information", description = "Manage enterprise clients and minimal accounts information")
@CrossOrigin( value = { "*" })
@RequiredArgsConstructor
public class EnterpriseClientController {

    public final EnterpriseClientServiceImpl service;

    @GetMapping("/{ruc}")
    public Mono<ResponseEntity<Mono<EnterpriseClient>>> getByRuc(@PathVariable String ruc) {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.getByRuc(ruc))
        );
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<Mono<EnterpriseClient>>> getById(@PathVariable String id) {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.getById(id))
        );
    }

    @GetMapping("/profiles")
    public Mono<ResponseEntity<Mono<String>>> getProfiles() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new Gson().toJson(ClientProfiles.getEnterpriseProfiles())))
        );
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<EnterpriseClient>>> getAll() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.getAll())
        );
    }

    @PostMapping
    public Mono<ResponseEntity<EnterpriseClient>> create(@RequestBody CreateEnterpriseClientDto o) {

        return service.save(o).map( p -> ResponseEntity
                .created(URI.create("/client/enterprise/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(p)
        );
    }

    @PostMapping(value = "/{ruc}/accounts")
    public Mono<ResponseEntity<EnterpriseClient>> addAccounts(@PathVariable String ruc, @RequestBody CreateEnterpriseClientAccountDto o) {

        return service.addAccount(ruc, o)
                .map(p -> ResponseEntity.created(URI.create("/client/enterprise/"
                                .concat(ruc)
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{ruc}/accounts")
    public Mono<ResponseEntity<EnterpriseClient>> updateAccounts(@PathVariable String ruc, @RequestBody UpdateEnterpriseClientAccountDto o) {

        return service.updateAccount(ruc, o)
                .map(p -> ResponseEntity.created(URI.create("/client/enterprise/"
                                .concat(ruc)
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{ruc}/accounts/{accountId}")
    public Mono<ResponseEntity<EnterpriseClient>> deleteAccounts(@PathVariable String ruc, @PathVariable String accountId) {

        return service.deleteAccount(ruc, accountId)
                .map(p -> ResponseEntity.created(URI.create("/client/enterprise/"
                                .concat(ruc)
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping
    public Mono<ResponseEntity<EnterpriseClient>> update(@RequestBody UpdateEnterpriseClientDto o) {
        return service.update(o)
                .map(p -> ResponseEntity.created(URI.create("/client/enterprise/"
                                .concat(p.getId())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<ResponseEntity<EnterpriseClient>> delete(@RequestBody DeleteEnterpriseClientDto o) {
        return service.delete(o)
                .map(p -> ResponseEntity.created(URI.create("/client/enterprise/"
                                .concat(p.getId())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}