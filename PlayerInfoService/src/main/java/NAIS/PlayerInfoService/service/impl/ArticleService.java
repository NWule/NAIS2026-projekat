package NAIS.PlayerInfoService.service.impl;

import NAIS.PlayerInfoService.dto.ArticleDTO;
import NAIS.PlayerInfoService.model.Article;
import NAIS.PlayerInfoService.model.Report;
import NAIS.PlayerInfoService.repository.ArticleRepository;
import NAIS.PlayerInfoService.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService {
    private final ArticleRepository articleRepo;

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

    public Article getArticleById(String id) {
        return articleRepo.findById(id).orElse(null);
    }

    public void deleteArticle(String id) {
        articleRepo.deleteById(id);
    }

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
}
