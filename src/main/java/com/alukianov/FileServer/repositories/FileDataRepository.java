package com.alukianov.FileServer.repositories;

import com.alukianov.FileServer.models.FileData;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long> {
    @NonNull
    Optional<FileData> findById(@NonNull Long id);
    @NonNull
    List<FileData> findAll();
}
