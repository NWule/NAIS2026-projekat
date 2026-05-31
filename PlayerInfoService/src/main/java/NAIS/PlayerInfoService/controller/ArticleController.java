package NAIS.PlayerInfoService.controller;

import NAIS.PlayerInfoService.dto.ArticleDTO;
import NAIS.PlayerInfoService.dto.MediaRiskResponse;
import NAIS.PlayerInfoService.model.Article;
import NAIS.PlayerInfoService.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final IArticleService articleService;

    @GetMapping("/all")
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable String id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<Article> saveArticle(@RequestBody ArticleDTO article) {
        return ResponseEntity.ok(articleService.saveArticle(article));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable String id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable String id, @RequestBody ArticleDTO article) {
        return ResponseEntity.ok(articleService.updateArticle(id, article));
    }

    @GetMapping("/media-risk/{playerId}")
    public ResponseEntity<MediaRiskResponse> getPlayerMediaRisk(@PathVariable String playerId) {
        MediaRiskResponse response = articleService.analyzePlayerMediaRisk(playerId);
        return ResponseEntity.ok(response);
    }
}
