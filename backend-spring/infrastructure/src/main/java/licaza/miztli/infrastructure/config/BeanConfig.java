package licaza.miztli.infrastructure.config;

import java.util.List;
import java.util.Map;
import java.util.function.*;
import licaza.miztli.app.usecase.*;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.*;
import licaza.miztli.infrastructure.adapters.PetImageS3StorageRepository;
import licaza.miztli.infrastructure.entrypoints.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;

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
  public S3Client s3Client() {
    return S3Client.builder().region(Region.US_EAST_1).build();
  }

  @Bean
  public StorageRepository storageRepository(S3Client s3Client) {
    return new PetImageS3StorageRepository(s3Client);
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
  public DeletePetByIdUseCase deletePetByIdUseCase(PetRepository repository) {
    return new DeletePetByIdUseCase(repository);
  }

  @Bean
  public GetAllPetsUseCase getAllPetsUseCase(PetRepository repository) {
    return new GetAllPetsUseCase(repository);
  }

  @Bean
  public UploadPetImagesUseCase uploadPetImageUseCase(StorageRepository storageRepository) {
    return new UploadPetImagesUseCase(storageRepository);
  }

  @Bean
  public Function<Message<PetImageRequest>, List<String>> uploadPetImages(
      UploadPetImagesUseCase useCase) {
    return message -> {
      PetImageRequest request = message.getPayload();
      return new UploadPetImageFunction(useCase).apply(message);
    };
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

  @Bean
  public Supplier<List<Pet>> findAll(GetAllPetsUseCase useCase) {
    return () -> useCase.execute();
  }

  @Bean
  public Function<Map<String, Object>, String> deletePetById(DeletePetByIdUseCase useCase) {
    return message -> {
      Map<String, String> pathParams = (Map<String, String>) message.get("pathParameters");

      if (pathParams == null || !pathParams.containsKey("id")) {
        throw new IllegalArgumentException("ID is required!");
      }

      String id = pathParams.get("id");
      useCase.execute(id);
      return "Pet with ID " + id + " correctly deleted!";
    };
  }
}
