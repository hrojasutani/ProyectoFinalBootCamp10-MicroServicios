package com.nttdata.client.personal.service;

import com.nttdata.client.personal.dto.*;
import com.nttdata.client.personal.entity.PersonalClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonalClientService {

    public Flux<PersonalClient> getAll();

    public Mono<PersonalClient> getById(String id);

    public Mono<PersonalClient> getByDocumentNumber(String documentNumber);

    public Mono<PersonalClient> save(CreatePersonalClientDto o);

    public Mono<PersonalClient> addAccount(String document, CreatePersonalClientAccountDto o);

    public Mono<PersonalClient> updateAccount(String document, UpdatePersonalClientAccountDto o);

    public Mono<PersonalClient> deleteAccount(String document, String accountId);

    public Mono<PersonalClient> update(UpdatePersonalClientDto o);

    public Mono<PersonalClient> delete(DeletePersonalClientDto o);
}