package com.zesty.ecom.Payload.Response;

import java.util.List;

import com.zesty.ecom.Payload.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
	private List<ProductDto> pageContent;
	private int pageNumber;
	private int pageSize;
	private int totalPages;
	private boolean lastPage;
}
