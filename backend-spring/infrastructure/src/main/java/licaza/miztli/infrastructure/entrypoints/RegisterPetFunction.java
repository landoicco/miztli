package licaza.miztli.infrastructure.entrypoints;

import java.util.function.Function;
import licaza.miztli.app.usecase.RegisterPetUseCase;
import licaza.miztli.domain.model.Pet;

public class RegisterPetFunction implements Function<PetRequest, Pet> {

  private final RegisterPetUseCase useCase;

  public RegisterPetFunction(RegisterPetUseCase useCase) {
    this.useCase = useCase;
  }

  @Override
  public Pet apply(PetRequest request) {
    return useCase.execute(request.name(), request.type(), request.age());
  }
}
