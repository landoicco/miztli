package licaza.miztli.app.usecase;

import java.util.UUID;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.PetRepository;

public class RegisterPetUseCase {

  private final PetRepository repository; // Inyectamos la interfaz

  public RegisterPetUseCase(PetRepository repository) {
    this.repository = repository;
  }

  public Pet execute(String name, String type, int age) {
    String normalizedType = type.trim().toUpperCase();

    String id = UUID.randomUUID().toString();

    Pet newPet = new Pet(id, name, type.toUpperCase(), age);

    repository.save(newPet);

    return newPet;
  }
}
