package sdi.servicedesk.services;

import io.minio.*;
import io.minio.messages.Item;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dto.ScreenShotDTO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:minio.properties")
public class MinioService {

    private static final Logger LOGGER = Logger.getLogger(MinioService.class);
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Autowired
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public List<ScreenShotDTO> getListObjects() {
        List<ScreenShotDTO> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> result = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .recursive(true)
                    .build());
            for (Result<Item> item : result) {
                objects.add(ScreenShotDTO.builder()
                        .filename(item.get().objectName())
                        .size(item.get().size())
                        .url(getPreSignedUrl(item.get().objectName()))
                        .build());
            }
            return objects;
        } catch (Exception e) {
            LOGGER.error("Happened error when get list objects from minio: ", e);
        }

        return objects;
    }

    public ScreenShotDTO uploadFile(ScreenShotDTO request) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(request.getFile().getOriginalFilename())
                    .stream(request.getFile().getInputStream(), request.getFile().getSize(), -1)
                    .build());
        } catch (Exception e) {
            LOGGER.error("Happened error when upload file: ", e);
        }
        return ScreenShotDTO.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .size(request.getFile().getSize())
                .url(getPreSignedUrl(request.getFile().getOriginalFilename()))
                .filename(request.getFile().getOriginalFilename())
                .build();
    }

    public InputStream getObject(String filename) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            LOGGER.error("Happened error when get list objects from minio: ", e);
            return null;
        }

        return stream;
    }

    private String getPreSignedUrl(String filename) {
        return "/files/".concat(filename);
    }

}
