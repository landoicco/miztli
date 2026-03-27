package licaza.miztli.domain.model;

import java.util.List;

public class Pet {
  private final String petId, name, type;
  private final List<String> imageUrls;
  private final int age;

  private Pet(Builder builder) {
    this.imageUrls = builder.imageUrls;
    this.petId = builder.petId;
    this.name = builder.name;
    this.type = builder.type;
    this.age = builder.age;
  }

  public List<String> getImageUrls() {
    return imageUrls;
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
    private List<String> imageUrls;
    private int age;

    public Builder petId(String petId) {
      this.petId = petId;
      return this;
    }

    public Builder imageUrls(List<String> urls) {
      this.imageUrls = urls;
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
