package NAIS.PlayerInfoService.controller;

import NAIS.PlayerInfoService.dto.ArticleDTO;
import NAIS.PlayerInfoService.model.Article;
import NAIS.PlayerInfoService.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final IArticleService articleService;

    @GetMapping("/all")
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable String id) {
        return articleService.getArticleById(id);
    }

    @PostMapping("/save")
    public Article saveArticle(@RequestBody ArticleDTO article) {
        return articleService.saveArticle(article);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteArticle(@PathVariable String id) {
        articleService.deleteArticle(id);
    }

    @PutMapping("/update/{id}")
    public Article updateArticle(@PathVariable String id, @RequestBody ArticleDTO article) {
        return articleService.updateArticle(id, article);
    }
}
