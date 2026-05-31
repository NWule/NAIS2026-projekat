package NAIS.PlayerInfoService.service;

import NAIS.PlayerInfoService.dto.ArticleDTO;
import NAIS.PlayerInfoService.model.Article;

import java.util.List;

public interface IArticleService {
    List<Article> getAllArticles();
    Article getArticleById(String id);
    Article saveArticle(ArticleDTO article);
    void deleteArticle(String id);
    Article updateArticle(String id, ArticleDTO article);
}
