package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Sizes;
import com.zesty.ecom.Payload.Dto.SizesDto;

public class SizeMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public SizesDto mapToDto(Sizes size) {
		return modelMapper.map(size,SizesDto.class);
	}
	
	public Sizes mapToEntity(SizesDto sizeDto) {
		return modelMapper.map(sizeDto, Sizes.class);
	}
	
	
}
