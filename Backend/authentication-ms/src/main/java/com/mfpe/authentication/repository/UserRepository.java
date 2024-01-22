package com.mfpe.authentication.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mfpe.authentication.model.AppUser;


@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {

}