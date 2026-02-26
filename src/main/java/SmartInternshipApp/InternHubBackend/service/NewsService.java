package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.News;
import SmartInternshipApp.InternHubBackend.dto.NewsDTO;
import SmartInternshipApp.InternHubBackend.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    public List<NewsDTO> getAllNews() {
        return newsRepository.findAll().stream()
            .map(news -> new NewsDTO(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getCompany() != null ? news.getCompany().getId() : null,
                news.getCompany() != null ? news.getCompany().getName() : null,
                news.getCreatedAt()
            ))
            .collect(Collectors.toList());
    }

    public News createNews(News news) {
        return newsRepository.save(news);
    }

    public News updateNews(Long id, News newsDetails) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        news.setTitle(newsDetails.getTitle());
        news.setContent(newsDetails.getContent());
        return newsRepository.save(news);
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}