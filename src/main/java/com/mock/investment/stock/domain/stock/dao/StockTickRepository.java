package com.mock.investment.stock.domain.stock.dao;

import com.mock.investment.stock.domain.stock.domain.Stock;
import com.mock.investment.stock.domain.stock.domain.StockTick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockTickRepository extends MongoRepository<StockTick, String> {
    @Aggregation(pipeline = {
            "{ $match: { symbol: { $regex: ?0 } } }",
            "{ $sort: { timestamp: -1 } }",
            "{ $group: { _id: '$symbol', doc: { $first: '$$ROOT' } } }",
            "{ $replaceRoot: { newRoot: '$doc' } }",
            "{ $sort: { turnover24h: -1 } }",
            "{ $skip: ?#{#pageable.offset} }",
            "{ $limit: ?#{#pageable.pageSize} }"
    })
    List<StockTick> findLatestQuoteStocksOrderByTurnover(@Param("quoteCoin") String quoteCoin, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: { symbol: { $regex: { $concat: ['.*', ?0, '$'] } } } }",
            "{ $sort: { timestamp: -1 } }",
            "{ $group: { _id: '$symbol' } }",
            "{ $count: 'total' }"
    })
    Long countLatestQuoteStocks(String quoteCoin);
}
