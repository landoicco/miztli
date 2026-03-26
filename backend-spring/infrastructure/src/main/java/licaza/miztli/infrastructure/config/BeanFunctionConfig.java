package licaza.miztli.infrastructure.config;

import java.util.List;
import java.util.Map;
import java.util.function.*;
import licaza.miztli.app.usecase.*;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.*;
import licaza.miztli.infrastructure.entrypoints.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

@Configuration
public class BeanFunctionConfig {
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

  @Bean
  public Function<Message<PetImageRequest>, List<String>> uploadPetImages(
      UploadPetImagesUseCase useCase) {
    return message -> {
      PetImageRequest request = message.getPayload();
      return new UploadPetImageFunction(useCase).apply(message);
    };
  }
}
