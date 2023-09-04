package com.gustavohenning.filehandler.service;

import com.gustavohenning.filehandler.entity.Attachment;
import com.gustavohenning.filehandler.repository.AttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence " + fileName);
            }

            Attachment attachment = new Attachment(fileName, file.getContentType(), file.getBytes());
            return attachmentRepository.save(attachment);
        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository.findById(fileId).orElseThrow(() -> new Exception("File Not Found with id: " + fileId)) ;
    }

    @Override
    public Attachment updateAttachment(String fileId, MultipartFile file) throws Exception {
        Attachment existingAttachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new Exception("File Not Found with id: " + fileId));

        String newFileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (newFileName.contains("..")) {
            throw new Exception("New filename contains invalid path sequence " + newFileName);
        }

        existingAttachment.setFileName(newFileName);
        existingAttachment.setFileType(file.getContentType());
        existingAttachment.setData(file.getBytes());

        return attachmentRepository.save(existingAttachment);
    }
}
