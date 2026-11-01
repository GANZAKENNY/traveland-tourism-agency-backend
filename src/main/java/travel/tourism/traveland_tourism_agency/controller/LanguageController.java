package travel.tourism.traveland_tourism_agency.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import travel.tourism.traveland_tourism_agency.domain.Language;
import travel.tourism.traveland_tourism_agency.service.LanguageService;
@RestController 
@RequestMapping("/api/languages")
public class LanguageController {

    @Autowired 
    private LanguageService languageService;

    @PostMapping("/save")
    public ResponseEntity<String> saveLanguage(@RequestBody Language language) {
        String result = languageService.saveLanguage(language);
        if (result.equals("Language saved successfully")) {
        return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    @GetMapping ("/all")
    public ResponseEntity<List<Language>> getAllLanguages() {
        return new ResponseEntity<>(languageService.getAllLanguages(), HttpStatus.OK);
}

   @GetMapping ("/unused")
   public ResponseEntity<List<Language>> getUnusedLanguages() {
        return new ResponseEntity<>(languageService.getUnusedLanguages(), HttpStatus.OK);
    }


    @GetMapping ("/{id}")
    public ResponseEntity<?> getLanguageById(@PathVariable UUID id) {
        Optional<Language> language = languageService.getLanguageById(id);
        if (language.isPresent()) {
            return new ResponseEntity<>(language.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The Language with that id doest not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping ("/update/{id}")
    public ResponseEntity<String> updateLanguage(@PathVariable UUID id, @RequestBody Language language) {
        String result = languageService.updateLanguage(id, language);
        if (result.equals("Language updated successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }


    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<String> deleteLanguage(@PathVariable UUID id) {
        String result = languageService.deleteLanguage(id);
        if (result.equals("Language deleted successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else if (result.equals("Some tour guides are still hold that Language")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}