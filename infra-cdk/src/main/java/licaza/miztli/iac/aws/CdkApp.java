package licaza.miztli.iac.aws;

import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.App;

public class CdkApp {
    public static void main(String[] args) {
        App app = new App();

        // Force deploy to us-east-1
        Environment env = Environment.builder()
            .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
            .region("us-east-1")
            .build();

        new MiztliStack(app, "MiztliStack", StackProps.builder()
                    .env(env)
                    .build());

        app.synth();
    }
}
