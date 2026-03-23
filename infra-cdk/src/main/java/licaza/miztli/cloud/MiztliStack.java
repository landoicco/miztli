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

        // Create DynamoDB table
        Table petsTable = Table.Builder.create(this, "PetsTable")
                .partitionKey(Attribute.builder().name("petId").type(AttributeType.STRING).build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        // Create Spring Lambda
        Function petsFunction = Function.Builder.create(this, "RegisterPetFunction")
                .runtime(Runtime.JAVA_17)
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                .code(Code.fromAsset("../miztli-lambda.jar"))
                .memorySize(2048)
                .timeout(Duration.seconds(30))
                .environment(Map.of(
                    "TABLE_NAME", petsTable.getTableName(),
                    "SPRING_CLOUD_FUNCTION_DEFINITION", "registerPet",
                    "MAIN_CLASS", "licaza.miztli.infrastructure.MiztliApp"
                ))
                .build();

        // Allow to write on table
        petsTable.grantWriteData(petsFunction);
    }
}
