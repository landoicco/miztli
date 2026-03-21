package licaza.miztli.domain.model;

import java.util.Objects;

/** Record that represent a basic "Pet" entity on the system */
public record Pet(String id, String name, String type, int age) {

  public Pet {
    Objects.requireNonNull(name, "Name can not be null!");
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name can not be empty!");
    }

    String upperType = type.toUpperCase();
    if (!upperType.equals("DOG") && !upperType.equals("CAT")) {
      throw new IllegalArgumentException("At the moment, we only support: DOG or CAT");
    }
  }
}
