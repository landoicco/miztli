package licaza.miztli.infrastructure.config;

import licaza.miztli.infrastructure.adapters.DynamoPetRepository;
import licaza.miztli.titlanoni.repository.PetRepository;
import licaza.miztli.titlanoni.usecase.RegisterPetUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class BeanConfig {

  @Bean
  public DynamoDbClient dynamoDbClient() {
    return DynamoDbClient.builder().build();
  }

  @Bean
  public PetRepository petRepository(DynamoDbClient client) {
    return new DynamoPetRepository(client);
  }

  @Bean
  public RegisterPetUseCase registerPetUseCase(PetRepository repository) {
    return new RegisterPetUseCase(repository);
  }
}
