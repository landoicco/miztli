package licaza.miztli.app.usecase;

import java.util.UUID;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.PetRepository;

public class RegisterPetUseCase {

  private final PetRepository repository;

  public RegisterPetUseCase(PetRepository repository) {
    this.repository = repository;
  }

  public Pet execute(String name, String type, int age) {
    String normalizedType = type.trim().toUpperCase();

    String id = "pet-" + UUID.randomUUID().toString();

    Pet newPet = Pet.builder().petId(id).name(name).type(type).age(age).build();

    repository.save(newPet);

    return newPet;
  }
}
