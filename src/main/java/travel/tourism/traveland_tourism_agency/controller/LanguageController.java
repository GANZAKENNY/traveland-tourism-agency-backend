package travel.tourism.traveland_tourism_agency.controller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RestController 
@RequestMapping("/api/languages")
public class LanguageController {

    @autowired
    private LanguageService languageService;

    @PostMapping("/save")
    public ResponseEntity<String> saveLanguage(@RequestBody Language language) {
        String result = languageService.saveLanguage(language);
        if (result.equals(anObject:"Language saved successfully")) {
        return new ResponseEntity.<>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity.<>(result, HttpStatus.CONFLICT);
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
    public ResponseEntity<Language> getLanguageById(@PathVariable UUID id) {
        Optional<Language> language = languageService.getLanguageById(id);
        if (language.isPresent()) {
            return new ResponseEntity<>(language.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(body:"The Language with that id doest not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping ("/update/{id}")
    public ResponseEntity<String> updateLanguage(@PathVariable UUID id, @RequestBody Language language) {
        String result = languageService.updateLanguage(id, language);
        if (result.equals(anObject:"Language updated successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }


    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<String> deleteLanguage(@PathVariable UUID id) {
        String result = languageService.deleteLanguage(id);
        if (result.equals(anObject:"Language deleted successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else if (result.equals(anObject:"Some tour guides are still hold that Language")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}