package project.general_project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.general_project.domain.Picture;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class PictureStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String name){
        return fileDir+name;
    }

    public Picture savePicture(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) return null;

        String originalFilename = multipartFile.getOriginalFilename();
        String saveFileName = createSaveFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(saveFileName)));

        return new Picture(originalFilename,saveFileName);
    }

    private static String createSaveFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid= UUID.randomUUID().toString();
        return uuid+"."+ext;
    }

    private static String extractExt(String originalFilename) {
        int index = originalFilename.lastIndexOf(".");
        return originalFilename.substring(index + 1);
    }
}
