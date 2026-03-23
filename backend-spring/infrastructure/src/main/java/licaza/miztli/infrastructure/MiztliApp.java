package licaza.miztli.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
    basePackages = {"licaza.miztli.infrastructure", "licaza.miztli.infrastructure.config"})
public class MiztliApp {
  public static void main(String[] args) {
    SpringApplication.run(MiztliApp.class, args);
  }
}
