package licaza.miztli.domain.repository;

import java.util.Optional;
import licaza.miztli.domain.model.Pet;

public interface PetRepository {
  void save(Pet pet);

  Optional<Pet> findById(String id);

  // List<Pet> findAll();
}
