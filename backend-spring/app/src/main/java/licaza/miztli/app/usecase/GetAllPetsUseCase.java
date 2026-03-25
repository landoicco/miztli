package licaza.miztli.app.usecase;

import java.util.List;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllPetsUseCase {

  private final PetRepository repository;

  public GetAllPetsUseCase(PetRepository repository) {
    this.repository = repository;
  }

  public List<Pet> execute() {
    return repository.findAll();
  }
}
