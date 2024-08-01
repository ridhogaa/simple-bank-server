package org.k1.simplebankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse<T> {

    private Integer currentPage;

    private Integer totalPage;

    private Integer size;

    private Integer totalItem;

    private T pagingData;
}
