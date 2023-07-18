package com.zesty.ecom.Payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
	private List<ProductDto> pageContent;
	private int pageNumber;
	private int pageSize;
	private int totalPages;
	private boolean lastPage;
}
