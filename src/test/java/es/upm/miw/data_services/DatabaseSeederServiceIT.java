package es.upm.miw.data_services;

import es.upm.miw.TestConfig;
import es.upm.miw.documents.ArticlesFamily;
import es.upm.miw.documents.FamilyType;
import es.upm.miw.documents.User;
import es.upm.miw.repositories.ArticlesFamilyRepository;
import es.upm.miw.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class DatabaseSeederServiceIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Autowired
    private ArticlesFamilyRepository articlesFamilyRepository;

    @Test
    void testDeleteAllAndInitialize() {
        this.databaseSeederService.deleteAllAndInitialize();
        assertFalse(userRepository.findByMobile("666666001").isPresent());
        this.databaseSeederService.seedDatabase();
        User user = userRepository.findByMobile("666666001").get();
        assertEquals("u001", user.getUsername());
        assertEquals("u001@gmail.com", user.getEmail());
        assertEquals("66666600L", user.getDni());
        assertTrue(user.getRoles().length >= 1);
    }

    @Test
    void testSeedDatabaseWithArticlesFamilies(){
        this.databaseSeederService.deleteAllAndInitialize();
        assertTrue(articlesFamilyRepository.findAll().isEmpty());
        this.databaseSeederService.seedDatabase();
        List<ArticlesFamily> articleFamily = this.articlesFamilyRepository.findByFamilyType(FamilyType.ARTICLES);
        assertEquals("root", articleFamily.get(0).getDescription());
    }

}
