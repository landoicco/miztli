package licaza.miztli.iac.aws;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigatewayv2.alpha.*;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.RemovalPolicy;
import software.constructs.Construct;
import java.util.Map;
import java.util.List;

public class MiztliStack extends Stack {
    public MiztliStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        /* Create PetsTable on Dynamo */
        Table petsTable = Table.Builder.create(this, "PetsTable")
                .partitionKey(Attribute.builder().name("petId").type(AttributeType.STRING).build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        new PetsApi(this, "PetsRestApi", petsTable);
    }
}
