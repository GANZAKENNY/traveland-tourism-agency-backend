package travel.tourism.traveland_tourism_agency.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travel.tourism.traveland_tourism_agency.domain.Language;
import travel.tourism.traveland_tourism_agency.repository.LanguageRepository;

@Service 
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

    public String saveLanguage(Language language) {
        if (languageRepository.existsByNameIgnoreCase(language.getName())) {
            return "That language already exists";
        }
        languageRepository.save(language);
        return "Language saved successfully";
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Optional<Language> getLanguageById(UUID id) {
        return languageRepository.findById(id);
    }

    public List<Language> getUnusedLanguages() {
        return languageRepository.findUnusedLanguages();
    }

    public String updateLanguage(UUID id, Language language) {
        Optional<Language> existing = languageRepository.findById(id);
        if (existing.isEmpty()) {
            return "The language with that id does not exist";
        }
        Language l = existing.get();
        l.setName(language.getName());
        languageRepository.save(l);
        return "Language updated successfully";
    }

    public String deleteLanguage(UUID id) {
        Optional<Language> language = languageRepository.findById(id);
        if (language.isEmpty()) {
            return "The language with that id does not exist";
        }
        if (!language.get().getTourGuides().isEmpty()) {
            return "Some tour guides still hold that language";
        }
        languageRepository.deleteById(id);
        return "Language deleted successfully";
    }
}
