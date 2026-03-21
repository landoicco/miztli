package licaza.miztli.titlanoni.repository;

import licaza.miztli.titlanoni.model.Pet;

public interface PetRepository {
  void save(Pet pet);

  // Optional<Pet> findById(String id);

  // List<Pet> findAll();
}
