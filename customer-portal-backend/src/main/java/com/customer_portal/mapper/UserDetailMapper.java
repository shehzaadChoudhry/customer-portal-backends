package com.customer_portal.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.customer_portal.dto.UserDetailDto;
import com.customer_portal.entity.UserDetail;

@Component
public class UserDetailMapper {
	
	private final ModelMapper modelMapper;
	
	public UserDetailMapper() {
		this.modelMapper = new ModelMapper();
	}
	
	public UserDetail toEntity(UserDetailDto userDetailDTO) {
        return modelMapper.map(userDetailDTO, UserDetail.class);
    }
	
	public UserDetailDto toDTO(UserDetail userDetail) {
        return modelMapper.map(userDetail, UserDetailDto.class);
    }
}
