package licaza.miztli.infrastructure.adapters;

import java.util.Map;
import java.util.Optional;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.PetRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class DynamoPetRepository implements PetRepository {

  private final DynamoDbClient dynamoDbClient;
  private final String tableName = System.getenv("TABLE_NAME");

  public DynamoPetRepository(DynamoDbClient dynamoDbClient) {
    this.dynamoDbClient = dynamoDbClient;
  }

  @Override
  public void save(Pet pet) {
    Map<String, AttributeValue> item =
        Map.of(
            "petId", AttributeValue.builder().s(pet.id()).build(),
            "name", AttributeValue.builder().s(pet.name()).build(),
            "type", AttributeValue.builder().s(pet.type()).build(),
            "age", AttributeValue.builder().n(String.valueOf(pet.age())).build());

    PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(item).build();

    // Push to the cloud!
    dynamoDbClient.putItem(request);
  }

  @Override
  public Optional<Pet> findById(String id) {
    Map<String, AttributeValue> key = Map.of("petId", AttributeValue.builder().s(id).build());
    GetItemRequest request =
        GetItemRequest.builder().tableName(System.getenv("TABLE_NAME")).key(key).build();
    GetItemResponse response = dynamoDbClient.getItem(request);

    if (!response.hasItem() || response.item().isEmpty()) {
      return Optional.empty();
    }

    String petId = response.item().get("petId").s(),
        name = response.item().get("name").s(),
        type = response.item().get("type").s();

    int age = Integer.parseInt(response.item().get("age").n());

    Pet pet = new Pet(petId, name, type, age);
    return Optional.of(pet);
  }
}
