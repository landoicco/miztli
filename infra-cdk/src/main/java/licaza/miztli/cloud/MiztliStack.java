package licaza.miztli.cloud;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.RemovalPolicy;
import software.constructs.Construct;
import java.util.Map;

public class MiztliStack extends Stack {
    public MiztliStack(final Construct scope, final String id) {
        super(scope, id);

        // 1. Crear Tabla de DynamoDB
        Table petsTable = Table.Builder.create(this, "PetsTable")
                .partitionKey(Attribute.builder().name("petId").type(AttributeType.STRING).build())
                .billingMode(BillingMode.PAY_PER_REQUEST) // Serverless: solo pagas lo que usas
                .removalPolicy(RemovalPolicy.DESTROY) // Solo para desarrollo
                .build();

        // 2. Crear la Lambda de Spring Boot
        Function petsFunction = Function.Builder.create(this, "RegisterPetFunction")
                .runtime(Runtime.JAVA_17)
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                // RUTA AL JAR DE TU OTRO PROYECTO
                .code(Code.fromAsset("../miztli-lambda.jar"))
                .memorySize(2048)
                .timeout(Duration.seconds(30))
                .environment(Map.of(
                    "TABLE_NAME", petsTable.getTableName(),
                    "SPRING_CLOUD_FUNCTION_DEFINITION", "registerPet",
                    "MAIN_CLASS", "licaza.miztli.infrastructure.MiztliApp"
                ))
                .build();

        // 3. Dar permisos a la Lambda para escribir en la tabla
        petsTable.grantWriteData(petsFunction);
    }
}
