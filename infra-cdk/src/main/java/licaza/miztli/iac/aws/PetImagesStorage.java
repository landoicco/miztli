package licaza.miztli.iac.aws;

import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.RemovalPolicy;
import software.constructs.Construct;
import java.util.Map;
import software.amazon.awscdk.Duration;

public class PetImagesStorage extends Construct {
    public PetImagesStorage(Construct scope, String id, Table table, Code lambdaCode) {
        super(scope, id);

        /* Pet images bucket on S3 */
        Bucket bucket = Bucket.Builder.create(this, "PetImagesBucket")
                .versioned(false) // Opcional: mantener versiones de fotos
                .publicReadAccess(true) // Importante para que las URLs funcionen en el frontend
                .removalPolicy(RemovalPolicy.DESTROY) // Solo para desarrollo (borra el bucket al destruir el stack)
                .blockPublicAccess(BlockPublicAccess.BLOCK_ACLS)
                .autoDeleteObjects(true)
                .build();


        /* Support image upload */

        // Lambda
        Function savePetImageFunction = Function.Builder.create(this, "SavePetImageFunction")
            .runtime(Runtime.JAVA_17)
            .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
            .code(lambdaCode)
            .timeout(Duration.seconds(30)) // Subir fotos toma tiempo
            .memorySize(2048) // Importante: Procesar Base64 requiere RAM
            .environment(Map.of(
                                // ESTA ES LA CLAVE: El nombre del @Bean en tu BeanConfig
                                "SPRING_CLOUD_FUNCTION_DEFINITION", "savePetImage",
                                "BUCKET_NAME", bucket.getBucketName(),
                                "SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING", "true",
                                "MAIN_CLASS", "licaza.miztli.infrastructure.MiztliApp"
                                ))
            .build();

        // 2. DAR PERMISOS (Sin esto fallará el UseCase)
        table.grantWriteData(savePetImageFunction);      // Para el repo de Dynamo
        bucket.grantWrite(savePetImageFunction);   // Para el repo de S3
    }
}
