package com.mock.investment.stock.domain.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockTickPageResponse<T> {
    private List<T> content;
    private long totalElements;
    private int pageNumber;
    private int pageSize;
    private boolean last;

    public static <T> StockTickPageResponse<T> of(List<T> content, long total, int pageNumber, int pageSize) {
        boolean isLast = (pageNumber + 1) * pageSize >= total;
        return new StockTickPageResponse<>(content, total, pageNumber, pageSize, isLast);
    }
}