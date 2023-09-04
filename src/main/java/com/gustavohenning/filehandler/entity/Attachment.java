package com.gustavohenning.filehandler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;


    @Entity
    @Data
    @NoArgsConstructor
    public class Attachment {

        @Id
        @UuidGenerator
        private String id;
        private String fileName;
        private String fileType;

        @Lob
        @Column(name="data", columnDefinition = "LONGBLOB")
        private byte[] data;

        public Attachment(String fileName, String fileType, byte[] data) {
            this.fileName = fileName;
            this.fileType = fileType;
            this.data = data;
        }



    }
