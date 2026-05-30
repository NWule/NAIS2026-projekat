package NAIS.PlayerInfoService.service.impl;

import NAIS.PlayerInfoService.repository.ArticleRepository;
import NAIS.PlayerInfoService.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService {
    private final ArticleRepository articleRepo;
}
