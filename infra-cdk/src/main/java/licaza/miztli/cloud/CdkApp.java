package licaza.miztli.cloud;

import software.amazon.awscdk.App;

public class CdkApp {
    public static void main(String[] args) {
        App app = new App();

        new MiztliStack(app, "MiztliStack");

        app.synth();
    }
}
