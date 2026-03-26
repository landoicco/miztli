package licaza.miztli.infrastructure.config;

import java.util.function.*;
import licaza.miztli.app.usecase.*;
import licaza.miztli.domain.repository.*;
import licaza.miztli.infrastructure.entrypoints.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanUseCaseConfig {
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
}
