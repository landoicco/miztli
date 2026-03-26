package licaza.miztli.infrastructure.adapters;

import licaza.miztli.domain.repository.StorageRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Repository
public class PetImageS3StorageRepository implements StorageRepository {
  private final S3Client s3Client;
  private final String bucketName;

  public PetImageS3StorageRepository(S3Client s3Client) {
    this.s3Client = s3Client;
    this.bucketName = System.getenv("BUCKET_NAME");
  }

  @Override
  public String uploadFile(String fileName, byte[] content) {
    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key("photos/" + fileName)
            .contentType("image/jpeg")
            .build(),
        RequestBody.fromBytes(content));

    return "https://" + bucketName + "://" + fileName;
  }
}
