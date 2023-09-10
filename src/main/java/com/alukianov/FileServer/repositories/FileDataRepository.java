package com.alukianov.FileServer.repositories;

import com.alukianov.FileServer.models.FileData;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    @NonNull
    Optional<FileData> findById(@NonNull Long id);
}
