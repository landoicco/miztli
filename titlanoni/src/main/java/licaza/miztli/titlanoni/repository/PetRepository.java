package licaza.miztli.titlanoni.repository;

import java.util.List;
import java.util.Optional;
import licaza.miztli.titlanoni.model.Pet;

public interface PetRepository {
  void save(Pet pet);

  Optional<Pet> findById(String id);

  List<Pet> findAll();
}
