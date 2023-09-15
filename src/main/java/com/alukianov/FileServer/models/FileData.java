package com.alukianov.FileServer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "files_data")
@NoArgsConstructor
@AllArgsConstructor
public class FileData {
    @Id
    @GeneratedValue(generator = "id_generator")
    @SequenceGenerator(name = "id_generator", sequenceName = "customer_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String extension;

    private String type;

    private Long size;

    @Column(name = "file_path")
    @JsonProperty(value = "file_path")
    private String filePath;

    @Column(name = "created_at")
    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;
}
