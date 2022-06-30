package com.nttdata.client.enterprise.service;

import com.nttdata.client.enterprise.dto.*;
import com.nttdata.client.enterprise.entity.EnterpriseClient;
// import com.nttdata.client.personal.dto.CreatePersonalClientAccountDto;
// import com.nttdata.client.personal.dto.UpdatePersonalClientAccountDto;
// import com.nttdata.client.personal.entity.PersonalClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EnterpriseClientService {

    public Flux<EnterpriseClient> getAll();

    public Mono<EnterpriseClient> getById(String id);

    public Mono<EnterpriseClient> getByRuc(String ruc);

    public Mono<EnterpriseClient> save(CreateEnterpriseClientDto o);

    public Mono<EnterpriseClient> addAccount(String document, CreateEnterpriseClientAccountDto o);

    public Mono<EnterpriseClient> updateAccount(String document, UpdateEnterpriseClientAccountDto o);

    public Mono<EnterpriseClient> deleteAccount(String document, String accountId);

    public Mono<EnterpriseClient> update(UpdateEnterpriseClientDto o);

    public Mono<EnterpriseClient> delete(DeleteEnterpriseClientDto o);
}
