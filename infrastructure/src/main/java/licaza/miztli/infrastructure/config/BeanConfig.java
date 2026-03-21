package licaza.miztli.infrastructure.config;

import java.util.function.Function;
import licaza.miztli.infrastructure.adapters.DynamoPetRepository;
import licaza.miztli.infrastructure.entrypoints.PetRequest;
import licaza.miztli.infrastructure.entrypoints.RegisterPetFunction;
import licaza.miztli.titlanoni.model.Pet;
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

  @Bean
  public Function<PetRequest, Pet> registerPet(RegisterPetUseCase useCase) {
    return new RegisterPetFunction(useCase);
  }
}
