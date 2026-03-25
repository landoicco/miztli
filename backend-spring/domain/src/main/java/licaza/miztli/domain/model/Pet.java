package licaza.miztli.domain.model;

public class Pet {
  private final String petId, name, type;
  private final int age;

  private Pet(Builder builder) {
    this.petId = builder.petId;
    this.name = builder.name;
    this.type = builder.type;
    this.age = builder.age;
  }

  public String getPetId() {
    return petId;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public int getAge() {
    return age;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String petId, name, type;
    private int age;

    public Builder petId(String petId) {
      this.petId = petId;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder age(int age) {
      this.age = age;
      return this;
    }

    public Pet build() {
      return new Pet(this);
    }
  }
}
