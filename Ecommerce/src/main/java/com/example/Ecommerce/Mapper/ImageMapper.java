package com.example.Ecommerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Ecommerce.Request.ImageDto;
import com.example.Ecommerce.model.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {
	
	@Mapping(source = "id", target = "imageId")
    @Mapping(source = "fileName", target = "imageName")
    @Mapping(source = "downloadUrl", target = "downloadUrl")
	ImageDto toDto(Image image);
	
	    @Mapping(source = "imageId", target = "id")
	    @Mapping(source = "imageName", target = "fileName")
	    @Mapping(source = "downloadUrl", target = "downloadUrl")
	Image toEntity(ImageDto imageDto);
	
	

}
