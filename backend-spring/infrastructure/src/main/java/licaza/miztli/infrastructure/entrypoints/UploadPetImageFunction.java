package licaza.miztli.infrastructure.entrypoints;

import java.util.List;
import java.util.function.Function;
import licaza.miztli.app.usecase.UploadPetImagesUseCase;
import org.springframework.messaging.Message;

public class UploadPetImageFunction implements Function<Message<PetImageRequest>, List<String>> {

  private final UploadPetImagesUseCase useCase;

  public UploadPetImageFunction(UploadPetImagesUseCase useCase) {
    this.useCase = useCase;
  }

  @Override
  public List<String> apply(Message<PetImageRequest> message) {

    PetImageRequest request = message.getPayload();
    return useCase.execute(request.petId(), request.photosBase64());
  }
}
