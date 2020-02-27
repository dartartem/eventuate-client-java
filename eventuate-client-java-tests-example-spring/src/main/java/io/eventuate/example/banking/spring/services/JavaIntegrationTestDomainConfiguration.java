package io.eventuate.example.banking.spring.services;

import io.eventuate.AggregateRepository;
import io.eventuate.EventuateAggregateStore;
import io.eventuate.example.banking.domain.Account;
import io.eventuate.example.banking.domain.AccountCommand;
import io.eventuate.example.banking.services.AccountCommandSideEventHandler;
import io.eventuate.example.banking.services.AccountQuerySideEventHandler;
import io.eventuate.example.banking.services.AccountService;
import io.eventuate.example.banking.services.MoneyTransferCommandSideEventHandler;
import io.eventuate.example.banking.services.counting.InvocationCounter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaIntegrationTestDomainConfiguration {

  @Bean
  public AccountCommandSideEventHandler accountCommandSideEventHandler() {
    return new AccountCommandSideEventHandler();
  }

  @Bean
  public MoneyTransferCommandSideEventHandler moneyTransferCommandSideEventHandler(InvocationCounter invocationCounter) {
    return new MoneyTransferCommandSideEventHandler(invocationCounter);
  }

  @Bean
  public AccountQuerySideEventHandler accountQuerySideEventHandler() {
    return new AccountQuerySideEventHandler();
  }

  @Bean
  public AccountService accountService(AggregateRepository<Account, AccountCommand> accountRepository) {
    return new AccountService(accountRepository);
  }

  @Bean
  public AggregateRepository<Account, AccountCommand> accountRepository(EventuateAggregateStore aggregateStore) {
    return new AggregateRepository<>(Account.class, aggregateStore);
  }

  @Bean
  public InvocationCounter invocationCounter() {
    return new InvocationCounter();
  }
}
