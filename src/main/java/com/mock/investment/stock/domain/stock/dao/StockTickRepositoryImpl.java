package com.mock.investment.stock.domain.stock.dao;

import com.mock.investment.stock.domain.stock.domain.StockTick;
import com.mock.investment.stock.domain.stock.dto.StockTickDto;
import com.mock.investment.stock.domain.stock.dto.StockTickPageResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockTickRepositoryImpl {
    private final MongoTemplate mongoTemplate;

    public StockTickPageResponse<StockTickDto> findLatestQuoteStocksOrderByTurnover(String quoteCoin, Pageable pageable) {
        String pattern = ".*" + quoteCoin + "$";

        // 데이터 조회
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("symbol").regex(pattern)),
                Aggregation.sort(Sort.Direction.DESC, "timestamp"),
                Aggregation.group("symbol")
                        .first("$$ROOT").as("doc"),
                Aggregation.replaceRoot("doc"),
                Aggregation.sort(Sort.Direction.DESC, "turnover24h"),
                Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()),
                Aggregation.limit(pageable.getPageSize())
        );

        List<StockTickDto> content = mongoTemplate.aggregate(
                aggregation,
                StockTick.class,
                StockTick.class
        ).getMappedResults().stream().map(StockTickDto::fromEntity).toList();

        // 전체 카운트 조회
        long total = countLatestQuoteStocks(quoteCoin);

        return StockTickPageResponse.of(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    public Long countLatestQuoteStocks(String quoteCoin) {
        String pattern = ".*" + quoteCoin + "$";

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("symbol").regex(pattern)),
                Aggregation.sort(Sort.Direction.DESC, "timestamp"),
                Aggregation.group("symbol"),
                Aggregation.count().as("total")
        );

        AggregationResults<CountResult> results = mongoTemplate.aggregate(
                aggregation,
                StockTick.class,
                CountResult.class
        );

        return results.getUniqueMappedResult() != null ? results.getUniqueMappedResult().getTotal() : 0L;
    }

    @Getter
    @NoArgsConstructor
    private static class CountResult {
        private Long total;
    }
}