package com.gustavohenning.filehandler.controller;

import com.gustavohenning.filehandler.ResponseData;
import com.gustavohenning.filehandler.entity.Attachment;
import com.gustavohenning.filehandler.service.AttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AttachemntController {

    private AttachmentService attachmentService;

    public AttachemntController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        Attachment attachment = null;
        String downloadUrl = "";
        String viewUrl = "";
        attachment = attachmentService.saveAttachment(file);
        downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachment.getId())
                .toUriString();
        viewUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/view/")
                .path(attachment.getId())
                .toUriString();

        return new ResponseData(attachment.getFileName(), downloadUrl, viewUrl, file.getContentType(), file.getSize());
    }

    @PutMapping("/update/{fileId}")
    public ResponseData updateFile(@PathVariable String fileId, @RequestParam("file") MultipartFile file) throws Exception {
        Attachment updatedAttachment = attachmentService.updateAttachment(fileId, file);
        String downloadUrl = "";
        String viewUrl = "";
        downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(updatedAttachment.getId())
                .toUriString();
        viewUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/view/")
                .path(updatedAttachment.getId())
                .toUriString();
        return new ResponseData(updatedAttachment.getFileName(), downloadUrl, viewUrl, updatedAttachment.getFileType(), file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .body(new ByteArrayResource(attachment.getData()));
    }
}
