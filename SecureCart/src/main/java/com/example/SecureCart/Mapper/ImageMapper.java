package com.example.SecureCart.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.SecureCart.Request.ImageDto;
import com.example.SecureCart.model.Image;

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
