package com.gustavohenning.filehandler.service;

import com.gustavohenning.filehandler.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

    Attachment updateAttachment(String fileId, MultipartFile file) throws Exception;
}
