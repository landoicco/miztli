package licaza.miztli.infrastructure.adapters;

import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey;

import java.util.List;
import java.util.stream.Collectors;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.PetRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class DynamoPetRepository implements PetRepository {

  private final DynamoDbTable<Pet> petTable;
  private final String tableName = System.getenv("TABLE_NAME");

  public DynamoPetRepository(DynamoDbEnhancedClient enhancedClient) {
    this.petTable = enhancedClient.table(tableName, PET_SCHEMA);
  }

  public void save(Pet pet) {
    petTable.putItem(pet);
  }

  public Pet findById(String id) {
    Key key = Key.builder().partitionValue(id).build();

    return petTable.getItem(key);
  }

  public List<Pet> findAll() {
    try {
      return petTable.scan().items().stream().collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Error at finding all pets on DynamoDB", e);
    }
  }

  public void delete(String id) {
    Key key = Key.builder().partitionValue(id).build();

    petTable.deleteItem(key);
  }

  // Define the Schema for our Pet POJO
  private static final TableSchema<Pet> PET_SCHEMA =
      TableSchema.builder(Pet.class, Pet.Builder.class)
          .newItemBuilder(Pet::builder, Pet.Builder::build)
          .addAttribute(
              String.class,
              a ->
                  a.name("petId")
                      .getter(Pet::getPetId)
                      .setter(Pet.Builder::petId)
                      .tags(primaryPartitionKey()))
          .addAttribute(
              String.class, a -> a.name("name").getter(Pet::getName).setter(Pet.Builder::name))
          .addAttribute(
              String.class, a -> a.name("type").getter(Pet::getType).setter(Pet.Builder::type))
          .addAttribute(
              Integer.class, a -> a.name("age").getter(Pet::getAge).setter(Pet.Builder::age))
          .build();
}
