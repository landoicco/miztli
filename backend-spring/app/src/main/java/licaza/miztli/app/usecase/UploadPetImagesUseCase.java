package licaza.miztli.app.usecase;

import java.util.Base64;
import java.util.List;
import java.util.UUID;
import licaza.miztli.domain.repository.*;
import org.springframework.stereotype.Service;

@Service
public class UploadPetImagesUseCase {
  private final StorageRepository storageRepository;

  public UploadPetImagesUseCase(StorageRepository storageRepository) {
    this.storageRepository = storageRepository;
  }

  public List<String> execute(String petId, List<String> photosBase64) {
    List<String> newUrls =
        photosBase64.stream()
            .map(
                base64 -> {
                  // Clean "raw" base64
                  String cleanBase64 = base64.replaceAll("[\\n\\r\\s]", ""),
                      fileName = petId + "/img-" + UUID.randomUUID() + ".jpg";
                  byte[] bytes = Base64.getDecoder().decode(cleanBase64);

                  return storageRepository.uploadFile(fileName, bytes);
                })
            .toList();

    return newUrls;
  }
}
