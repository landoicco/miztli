package licaza.miztli.infrastructure.config;

import java.util.function.Function;
import licaza.miztli.app.usecase.RegisterPetUseCase;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.PetRepository;
import licaza.miztli.infrastructure.adapters.DynamoPetRepository;
import licaza.miztli.infrastructure.entrypoints.PetRequest;
import licaza.miztli.infrastructure.entrypoints.RegisterPetFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
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
  public Function<Message<PetRequest>, Pet> registerPet(RegisterPetUseCase useCase) {
    return message -> {
      PetRequest request = message.getPayload();
      return new RegisterPetFunction(useCase).apply(message);
    };
  }
}
