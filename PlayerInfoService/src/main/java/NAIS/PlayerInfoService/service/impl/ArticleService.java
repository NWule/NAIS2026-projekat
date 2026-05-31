package NAIS.PlayerInfoService.service.impl;

import NAIS.PlayerInfoService.dto.ArticleDTO;
import NAIS.PlayerInfoService.dto.MediaRiskResponse;
import NAIS.PlayerInfoService.model.Article;
import NAIS.PlayerInfoService.repository.ArticleRepository;
import NAIS.PlayerInfoService.service.IArticleService;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService {
    private final ArticleRepository articleRepo;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @CacheEvict(value = "mediaRiskCache", key = "#article.playerId")
    public Article saveArticle(ArticleDTO article) {
        Article newArticle = new Article();
        newArticle.setPlayerId(article.getPlayerId());
        newArticle.setTitle(article.getTitle());
        newArticle.setContent(article.getContent());
        newArticle.setSource(article.getSource());
        newArticle.setSentimentScore(article.getSentimentScore());
        newArticle.setPublishDate(article.getPublishDate());
        return articleRepo.save(newArticle);
    }

    public List<Article> getAllArticles() {
        Iterable<Article> iterable = articleRepo.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    @Cacheable(value = "singleArticleCache", key = "#id", unless = "#result == null")
    public Article getArticleById(String id) {
        return articleRepo.findById(id).orElse(null);
    }

    public void deleteArticle(String id) {
        articleRepo.deleteById(id);
    }

    @Caching(
            put = {
                    @CachePut(value = "singleArticleCache", key = "#id", unless = "#result == null")
            },
            evict = {
                    @CacheEvict(value = "mediaRiskCache", allEntries = true),
            }
    )
    public Article updateArticle(String id, ArticleDTO article) {
        Article existingArticle = articleRepo.findById(id).orElse(null);
        if (existingArticle != null) {
            if (!article.getPlayerId().isEmpty()) {
                existingArticle.setPlayerId(article.getPlayerId());
            }
            if (!article.getTitle().isEmpty()) {
                existingArticle.setTitle(article.getTitle());
            }
            if (!article.getContent().isEmpty()) {
                existingArticle.setContent(article.getContent());
            }
            if (!article.getSource().isEmpty()) {
                existingArticle.setSource(article.getSource());
            }
            if (article.getSentimentScore() != null) {
                existingArticle.setSentimentScore(article.getSentimentScore());
            }
            if (article.getPublishDate() != null) {
                existingArticle.setPublishDate(article.getPublishDate());
            }
        }
        return articleRepo.save(existingArticle);
    }

    @Cacheable(
            value = "mediaRiskCache",
            key = "#playerId",
            unless = "#result == null"
    )
    public MediaRiskResponse analyzePlayerMediaRisk(String playerId) {
        Query playerFilter = Query.of(q -> q.term(t -> t.field("playerId").value(playerId)));
        Query mediaTextSearch = Query.of(q -> q.multiMatch(m -> m.fields("title", "content").query("injury incident")));
        Query dateRangeFilter = Query.of(q -> q.range(r -> r.date(d -> d.field("publishDate").gte("now-2y"))));

        Query boolQuery = Query.of(b -> b.bool(bool -> bool
                .filter(playerFilter, dateRangeFilter)
                .must(mediaTextSearch)
        ));

        Aggregation sourcesSubAgg = Aggregation.of(a -> a.terms(t -> t.field("source")));

        Aggregation dateHistogramAgg = Aggregation.of(a -> a.dateHistogram(d -> d.field("publishDate")
                        .calendarInterval(CalendarInterval.Month))
                .aggregations("sources", sourcesSubAgg));

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .withSort(s -> s.field(f -> f.field("publishDate").order(SortOrder.Desc)))
                .withAggregation("risk-chronology", dateHistogramAgg)
                .build();

        return convertSearchHitsToMediaRiskResponse(elasticsearchTemplate.search(nativeQuery, Article.class));
    }

    public MediaRiskResponse convertSearchHitsToMediaRiskResponse(SearchHits<Article> hits) {
        List<Article> articles = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        List<MediaRiskResponse.MonthlyRisk> timeline = new ArrayList<>();

        if (hits.hasAggregations()) {
            ElasticsearchAggregations esAggs = (ElasticsearchAggregations) hits.getAggregations();
            Map<String, ElasticsearchAggregation> aggMap = esAggs.aggregationsAsMap();

            Aggregate hronologija = aggMap.get("risk-chronology").aggregation().getAggregate();

            if (hronologija != null && hronologija.isDateHistogram()) {
                for (DateHistogramBucket monthBucket : hronologija.dateHistogram().buckets().array()) {
                    String month = monthBucket.keyAsString();
                    Map<String, Long> sourcesData = new HashMap<>();

                    Aggregate izvori = monthBucket.aggregations().get("sources");
                    if (izvori != null && izvori.isSterms()) {
                        for (StringTermsBucket sourceBucket : izvori.sterms().buckets().array()) {
                            sourcesData.put(sourceBucket.key().stringValue(), sourceBucket.docCount());
                        }
                    }
                    timeline.add(new MediaRiskResponse.MonthlyRisk(month, sourcesData));
                }
            }
        }

        return new MediaRiskResponse(articles, timeline);
    }
}
