package licaza.miztli.infrastructure.config;

import java.util.Map;
import java.util.function.Function;
import licaza.miztli.app.usecase.GetPetByIdUseCase;
import licaza.miztli.app.usecase.RegisterPetUseCase;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.PetRepository;
import licaza.miztli.infrastructure.entrypoints.PetRequest;
import licaza.miztli.infrastructure.entrypoints.RegisterPetFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class BeanConfig {

  @Bean
  public DynamoDbClient dynamoDbClient() {
    return DynamoDbClient.builder().build();
  }

  @Bean
  public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
    return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
  }

  @Bean
  public RegisterPetUseCase registerPetUseCase(PetRepository repository) {
    return new RegisterPetUseCase(repository);
  }

  @Bean
  public GetPetByIdUseCase getPetByIdUseCase(PetRepository repository) {
    return new GetPetByIdUseCase(repository);
  }

  @Bean
  public Function<Message<PetRequest>, Pet> registerPet(RegisterPetUseCase useCase) {
    return message -> {
      PetRequest request = message.getPayload();
      return new RegisterPetFunction(useCase).apply(message);
    };
  }

  @Bean
  public Function<Map<String, Object>, Pet> getPetById(GetPetByIdUseCase useCase) {
    return message -> {
      Map<String, String> pathParams = (Map<String, String>) message.get("pathParameters");

      if (pathParams == null || !pathParams.containsKey("id")) {
        throw new IllegalArgumentException("ID is required!");
      }

      String id = pathParams.get("id");

      return useCase.execute(id);
    };
  }
}
