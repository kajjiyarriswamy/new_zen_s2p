package com.user.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.Entity.UserDetailsEntity;


@Repository
public interface UserMappingRepository
        extends JpaRepository<UserDetailsEntity, Long> {

    Optional<UserDetailsEntity> findByUserId(String userId);
}
