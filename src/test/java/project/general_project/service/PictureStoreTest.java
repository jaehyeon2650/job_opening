package project.general_project.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import project.general_project.domain.Picture;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PictureStoreTest {

    @Autowired
    PictureStore pictureStore;

    @Test
    public void 전체_경로_조회() throws Exception{
        //given
        String fileName="image.jpg";
        //when
        String fullPath = pictureStore.getFullPath(fileName);
        //then
        assertThat(fullPath).isEqualTo("C:/Users/82102/Desktop/images/image.jpg");
    }

    @Test
    public void 사진_저장() throws Exception{
        //given
        // 모의 객체 생성(Mock)
        MultipartFile multipartFile = mock(MultipartFile.class);
        // 메서드 호출 예상 동작 설정(Stub)
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test-image.png");
        //when
        Picture picture = pictureStore.savePicture(multipartFile);
        //then
        assertThat(picture).isNotNull();
        assertThat(picture.getOriginalName()).isEqualTo("test-image.png");
        assertThat(picture.getSaveName()).isNotNull();

        //메서드 호출 검증(Verify)
        verify(multipartFile,times(1)).transferTo(any(File.class));
    }

}