package licaza.miztli.app.usecase;

import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class GetPetByIdUseCase {

  private final PetRepository repository;

  public GetPetByIdUseCase(PetRepository repository) {
    this.repository = repository;
  }

  public Pet execute(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("No pet found with ID: " + id));
  }
}
