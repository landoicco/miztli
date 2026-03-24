package licaza.miztli.infrastructure.entrypoints;

import java.util.function.Function;
import licaza.miztli.app.usecase.RegisterPetUseCase;
import licaza.miztli.domain.model.Pet;
import org.springframework.messaging.Message;

public class RegisterPetFunction implements Function<Message<PetRequest>, Pet> {

  private final RegisterPetUseCase useCase;

  public RegisterPetFunction(RegisterPetUseCase useCase) {
    this.useCase = useCase;
  }

  @Override
  public Pet apply(Message<PetRequest> message) {

    PetRequest request = message.getPayload();
    return useCase.execute(request.name(), request.type(), request.age());
  }
}
