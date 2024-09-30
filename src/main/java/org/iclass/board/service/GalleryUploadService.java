package org.iclass.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.GalleryDTO;
import org.iclass.board.entity.GalleryEntity;
import org.iclass.board.repository.GalleryUploadRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class GalleryUploadService {

    private final GalleryUploadRepository uploadRepository;

    public void uploadGallery(GalleryDTO dto) throws IOException{
        MultipartFile file = dto.getFile();
        // 서버 디렉토리 위치 c:\\upload
        String path = "c:\\upload";
        if(file.getSize() !=0) {
            //서버디렉토리에 저장은 java.io.File 객체를 생성합니다.
            File pathFile = new File(path + "\\" + file.getOriginalFilename());

            // 파일 전송 MultipartFile 객체를 파일시스템으로 저장(전송)
            file.transferTo(pathFile);

            // db 에 저장할 파일명 저장.
            dto.setFileNames(file.getOriginalFilename());

            // db 테이블에 저장될 값 확인
            log.info("dto {}", dto);
            uploadRepository.save(dto.toEntity());
        }
    }

    // 람다식 안에서 리턴 받을 변수는 전역 변수만 가능
    // 람다식은 함수형 인터페이스(추상메서드가 1개 인터페이스)를 익명클래스로 구현한 것
    GalleryDTO dto = null;
    public GalleryDTO one(int i) {

        Optional<GalleryEntity> optional
                = uploadRepository.findById(i);
        optional.ifPresent ( o -> {
            GalleryEntity entity = optional.get();
            dto = GalleryDTO.of(entity);
        });
        log.info("dto: {}", dto);
        return dto;
    }
}