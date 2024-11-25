package com.praveen.Spring_FileStorage_Directory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.praveen.Spring_FileStorage_Directory.entity.Filee;

@Repository
public interface FileRepository extends JpaRepository<Filee, Integer> {

}
