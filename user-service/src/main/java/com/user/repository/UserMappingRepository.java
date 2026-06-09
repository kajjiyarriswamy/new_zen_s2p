package com.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.Entity.UserDetailsEntity;

public interface UserMappingRepository extends JpaRepository<UserDetailsEntity, Integer>{

	Optional<UserDetailsEntity> findByUserId(String userId);

}
