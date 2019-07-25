package com.eisen.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import com.eisen.entity.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();
    User findByPhone(String phone);
    List<User> findByDate(String date);

    @Modifying
    @Transactional
    void removeByPhone(String phone);
    @Override
    <S extends User> S save(S s);
};
