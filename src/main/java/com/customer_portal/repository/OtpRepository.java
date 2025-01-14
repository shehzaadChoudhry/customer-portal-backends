package com.customer_portal.repository;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.customer_portal.entity.OtpDetail;

@Transactional
@Repository
public interface OtpRepository extends JpaRepository<OtpDetail, Long>{

	@Query(value = "Select * from otp_detail where user_email = :email and otp = :otp ",nativeQuery = true)
	OtpDetail findOtpByEmailOtp(@Valid String email, @Valid String otp);

}
