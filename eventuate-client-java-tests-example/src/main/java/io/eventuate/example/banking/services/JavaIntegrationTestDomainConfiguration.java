package io.eventuate.example.banking.services;

import io.eventuate.AggregateRepository;
import io.eventuate.EventuateAggregateStore;
import io.eventuate.example.banking.domain.Account;
import io.eventuate.example.banking.domain.AccountCommand;
import io.eventuate.example.banking.services.counting.InvocationCountingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class JavaIntegrationTestDomainConfiguration {

  @Bean
  public AccountCommandSideEventHandler accountCommandSideEventHandler() {
    return new AccountCommandSideEventHandler();
  }

  @Bean
  public MoneyTransferCommandSideEventHandler moneyTransferCommandSideEventHandler() {
    return new MoneyTransferCommandSideEventHandler();
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
  public InvocationCountingAspect loggingAspect() {
    return new InvocationCountingAspect();
  }

}
