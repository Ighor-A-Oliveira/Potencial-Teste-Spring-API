package com.ighor.jwt_demo.repository;

import com.ighor.jwt_demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //here we are returning a UserDetails because this class is implemented by the User Class
    // and it has all the auth details of our user
    Optional<UserDetails> findUserByEmail(String userName);

}