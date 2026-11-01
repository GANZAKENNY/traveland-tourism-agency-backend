package travel.tourism.traveland_tourism_agency.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import travel.tourism.traveland_tourism_agency.domain.Language;



@Repository 
public interface LanguageRepository extends JpaRepository<Language, UUID> {
    
    boolean existsByNameIgnoreCase(String name);

    @Query ("SELECT l FROM Language l WHERE l.tourGuides IS EMPTY")
    List<Language> findUnusedLanguages();
}
