package licaza.miztli.infrastructure.adapters;

import java.util.Map;
import licaza.miztli.domain.model.Pet;
import licaza.miztli.domain.repository.PetRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class DynamoPetRepository implements PetRepository {

  private final DynamoDbClient dynamoDbClient;
  // Esta variable vendrá de la configuración de AWS Lambda (CDK)
  private final String tableName = System.getenv("TABLE_NAME");

  // Constructor manual para evitar el error de variable no inicializada
  public DynamoPetRepository(DynamoDbClient dynamoDbClient) {
    this.dynamoDbClient = dynamoDbClient;
  }

  @Override
  public void save(Pet pet) {
    // Convertimos nuestro Record Pet a un mapa que DynamoDB entienda
    Map<String, AttributeValue> item =
        Map.of(
            "petId", AttributeValue.builder().s(pet.id()).build(),
            "name", AttributeValue.builder().s(pet.name()).build(),
            "type", AttributeValue.builder().s(pet.type()).build(),
            "age", AttributeValue.builder().n(String.valueOf(pet.age())).build());

    PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(item).build();

    // Guardamos en la nube
    dynamoDbClient.putItem(request);
  }
}
