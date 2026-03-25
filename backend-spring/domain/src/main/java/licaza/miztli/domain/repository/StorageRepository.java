package licaza.miztli.domain.repository;

public interface StorageRepository {
  String uploadFile(String fileName, byte[] content);
}
