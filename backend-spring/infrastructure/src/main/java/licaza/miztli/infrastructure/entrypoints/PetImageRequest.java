package licaza.miztli.infrastructure.entrypoints;

import java.util.List;

public record PetImageRequest(String petId, List<String> photosBase64) {}
