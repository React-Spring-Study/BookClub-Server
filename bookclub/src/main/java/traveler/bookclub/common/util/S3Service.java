package traveler.bookclub.common.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import traveler.bookclub.common.exception.S3ErrorCode;
import traveler.bookclub.common.exception.S3Exception;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String uploadClubImage(Long clubId, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storeFileName = "club" + clubId.toString() + "." + ext;
        String key = "clubs/" + storeFileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, setMetaData(multipartFile))
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new S3Exception(S3ErrorCode.S3_UPLOAD_FAILED);
        }

        log.info("type: {}", amazonS3Client.getObjectMetadata(bucket, key).getContentType());
        return amazonS3Client.getUrl(bucket, key).toString();
    }

    @Transactional
    public String uploadReviewImage(Long reviewId, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storeFileName = "review" + reviewId.toString() + "." + ext;
        String key = "reviews/" + storeFileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, setMetaData(multipartFile))
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new S3Exception(S3ErrorCode.S3_UPLOAD_FAILED);
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    @Transactional
    public void deleteImage(String key) {
        try{
            amazonS3Client.deleteObject(bucket, key.substring(52));
        } catch (Exception ex) {
            log.error("S3 Delete Error: {}", ex.getMessage());
            throw new S3Exception(S3ErrorCode.S3_DELETE_FAILED);
        }
    }

    private ObjectMetadata setMetaData(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/png");
        objectMetadata.setContentLength(multipartFile.getSize());

        return objectMetadata;
    }
}
