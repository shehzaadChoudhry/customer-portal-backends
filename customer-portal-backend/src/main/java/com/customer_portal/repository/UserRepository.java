package com.customer_portal.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.customer_portal.entity.UserDetail;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<UserDetail, Long>{

	@Query(value = "SELECT COUNT(*) USER_DETAIL WHERE USER_EMAIL = :email ", nativeQuery = true)
	Integer checkUserIsActive(String email);

	@Query(value = "SELECT * FROM USER_DETAIL WHERE USER_EMAIL = :email AND USER_PASSWORD = :password ",nativeQuery = true)
	UserDetail findUserByUserNameAndPassword(String email, String password);
	
}
