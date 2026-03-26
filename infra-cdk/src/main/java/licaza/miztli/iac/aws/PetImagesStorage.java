package licaza.miztli.iac.aws;

import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.apigatewayv2.alpha.*;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.constructs.Construct;
import java.util.Map;
import java.util.List;
import software.amazon.awscdk.Duration;

public class PetImagesStorage extends Construct {
    public PetImagesStorage(Construct scope, String id, HttpApi httpApi, Code lambdaCode) {
        super(scope, id);

        /* Pet images bucket on S3 */
        Bucket bucket = Bucket.Builder.create(this, "PetImagesBucket")
                .versioned(false)
                .publicReadAccess(true)
                .removalPolicy(RemovalPolicy.DESTROY)
                .blockPublicAccess(BlockPublicAccess.BLOCK_ACLS)
                .autoDeleteObjects(true)
                .build();


        /* Support image upload */

        // Lambda
        Function savePetImageFunction = Function.Builder.create(this, "SavePetImageFunction")
            .runtime(Runtime.JAVA_17)
            .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
            .code(lambdaCode)
            .timeout(Duration.seconds(30))
            .memorySize(2048)
            .environment(Map.of(
                                "SPRING_CLOUD_FUNCTION_DEFINITION", "uploadPetImages",
                                "BUCKET_NAME", bucket.getBucketName(),
                                "SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING", "true",
                                "MAIN_CLASS", "licaza.miztli.infrastructure.MiztliApp"
                                ))
            .build();

        bucket.grantWrite(savePetImageFunction);
        HttpLambdaIntegration StoreInS3LambdaIntegration = HttpLambdaIntegration.Builder.create("SavePetImageFunctionLambdaIntegration", savePetImageFunction).build();

         httpApi.addRoutes(AddRoutesOptions.builder()
                          .path("/pet/add/img/{id}")
                          .methods(List.of(software.amazon.awscdk.services.apigatewayv2.alpha.HttpMethod.POST))
                          .integration(StoreInS3LambdaIntegration)
                          .build());
    }
}
