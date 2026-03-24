package licaza.miztli.cloud;

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

public class PetsApi extends Construct {

    public PetsApi(Construct scope, String id, Table table) {
        super(scope, id);

        Code lambdaCode = Code.fromAsset("../miztli-lambda.jar");

         /* Define ApiGateway */
        HttpApi httpApi = HttpApi.Builder.create(this, "MiztliApi").build();

        /* Support POST */

        // Lambda
        Function postFunction = Function.Builder.create(this, "RegisterPetFunction")
                .runtime(Runtime.JAVA_17)
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
                .code(lambdaCode)
                .memorySize(2048)
                .timeout(Duration.seconds(30))
                .environment(Map.of(
                    "TABLE_NAME", table.getTableName(),
                    "SPRING_CLOUD_FUNCTION_DEFINITION", "registerPet",
                    "SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING", "true",
                    "MAIN_CLASS", "licaza.miztli.infrastructure.MiztliApp"
                ))
                .build();

        HttpLambdaIntegration postLambdaIntegration = HttpLambdaIntegration.Builder.create("PostFunctionIntegration", postFunction).build();

        // Allow to write on table
        table.grantWriteData(postFunction);

        httpApi.addRoutes(AddRoutesOptions.builder()
                          .path("/pet/add")
                          .methods(List.of(software.amazon.awscdk.services.apigatewayv2.alpha.HttpMethod.POST))
                          .integration(postLambdaIntegration)
                          .build());

        /* Support GET */

        // Lambda
        Function getByIdFunction = Function.Builder.create(this, "GetPetByIdFunction")
            .runtime(Runtime.JAVA_17)
            .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
            .code(lambdaCode)
            .memorySize(2048)
            .timeout(Duration.seconds(30))
            .environment(Map.of(
                                "TABLE_NAME", table.getTableName(),
                                "SPRING_CLOUD_FUNCTION_DEFINITION", "getPetById",
                                "SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING", "true",
                                "MAIN_CLASS", "licaza.miztli.infrastructure.MiztliApp"
                                ))
            .build();

        HttpLambdaIntegration getByIdLambdaIntegration = HttpLambdaIntegration.Builder.create("GetPetByIdFunctionIntegration", getByIdFunction).build();

        table.grantReadData(getByIdFunction);

        // By petId
        httpApi.addRoutes(AddRoutesOptions.builder()
                          .path("/pet/{id}")
                          .methods(List.of(software.amazon.awscdk.services.apigatewayv2.alpha.HttpMethod.GET))
                          .integration(getByIdLambdaIntegration)
                          .build());

        // All pets

        // Lambda
        Function getAllPetsFunction = Function.Builder.create(this, "GetAllPetsFunction")
            .runtime(Runtime.JAVA_17)
            .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
            .code(lambdaCode)
            .memorySize(2048)
            .timeout(Duration.seconds(30))
            .environment(Map.of(
                                "TABLE_NAME", table.getTableName(),
                                "SPRING_CLOUD_FUNCTION_DEFINITION", "findAll",
                                "SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING", "true",
                                "MAIN_CLASS", "licaza.miztli.infrastructure.MiztliApp"
                                ))
            .build();

        HttpLambdaIntegration getAllPetsLambdaIntegration = HttpLambdaIntegration.Builder.create("GetAllPetsFunctionIntegration", getAllPetsFunction).build();

        table.grantReadData(getAllPetsFunction);


        httpApi.addRoutes(AddRoutesOptions.builder()
                          .path("/pets")
                          .methods(List.of(software.amazon.awscdk.services.apigatewayv2.alpha.HttpMethod.GET))
                          .integration(getAllPetsLambdaIntegration)
                          .build());

    /* Support DELETE */

        // Lambda
        Function deleteByIdFunction = Function.Builder.create(this, "DeletePetByIdFunction")
            .runtime(Runtime.JAVA_17)
            .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
            .code(lambdaCode)
            .memorySize(2048)
            .timeout(Duration.seconds(30))
            .environment(Map.of(
                                "TABLE_NAME", table.getTableName(),
                                "SPRING_CLOUD_FUNCTION_DEFINITION", "deletePetById",
                                "SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING", "true",
                                "MAIN_CLASS", "licaza.miztli.infrastructure.MiztliApp"
                                ))
            .build();

        HttpLambdaIntegration deleteByIdLambdaIntegration = HttpLambdaIntegration.Builder.create("DeletePetByIdFunctionIntegration", deleteByIdFunction).build();

        table.grantWriteData(deleteByIdFunction);

        // By petId
        httpApi.addRoutes(AddRoutesOptions.builder()
                          .path("/pet/remove/{id}")
                          .methods(List.of(software.amazon.awscdk.services.apigatewayv2.alpha.HttpMethod.DELETE))
                          .integration(deleteByIdLambdaIntegration)
                          .build());
        }
}
