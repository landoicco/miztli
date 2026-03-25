package licaza.miztli.app.usecase;

import licaza.miztli.domain.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class DeletePetByIdUseCase {

  private final PetRepository repository;

  public DeletePetByIdUseCase(PetRepository repository) {
    this.repository = repository;
  }

  public void execute(String id) {
    repository.delete(id);
  }
}
