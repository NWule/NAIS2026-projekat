package NAIS.PlayerInfoService.repository;

import NAIS.PlayerInfoService.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends ElasticsearchRepository<Report, String> {

}
