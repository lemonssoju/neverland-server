package com.lesso.neverland.common.image;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    public String uploadImage(String directoryName, MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        String fullPath = directoryName+"/"+fileName;
        long size = multipartFile.getSize();

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(size);

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fullPath, multipartFile.getInputStream(), objectMetaData)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String imagePath = amazonS3Client.getUrl(bucketName, fullPath).toString(); // TODO: 전체 url 반환하는지 확인
        System.out.println("imagePath = " + imagePath);
        return imagePath;
    }

    public void deleteImage(String imagePath) {
        try {
            URL url = new URL(imagePath);
            String key = url.getPath().substring(bucketName.length()+2);

            amazonS3Client.deleteObject(bucketName, key);
        } catch (AmazonServiceException e) {
            throw new IllegalArgumentException(e.getErrorMessage());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
