package es.upm.miw.restControllers;

import es.upm.miw.dataServices.DatabaseSeederService;
import es.upm.miw.dtos.TokenOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RestService {

    @Autowired
    private Environment environment;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Value("${server.contextPath}")
    private String contextPath;

    @Value("${miw.admin.mobile}")
    private String adminMobile;

    @Value("${miw.admin.password}")
    private String adminPassword;

    private TokenOutputDto tokenDto;

    @PostConstruct
    public void constructor() {
        this.reLoadTestDB();
    }

    private int port() {
        return Integer.parseInt(environment.getProperty("local.server.port"));
    }

    public <T> RestBuilder<T> restBuilder(RestBuilder<T> restBuilder) {
        restBuilder.port(this.port());
        restBuilder.path(contextPath);
        if (tokenDto != null) {
            restBuilder.basicAuth(tokenDto.getToken());
        }
        return restBuilder;
    }

    public RestBuilder<Object> restBuilder() {
        RestBuilder<Object> restBuilder = new RestBuilder<>(this.port());
        restBuilder.path(contextPath);
        if (tokenDto != null) {
            restBuilder.basicAuth(tokenDto.getToken());
        }
        return restBuilder;
    }

    public RestService logout() {
        this.tokenDto = null;
        return this;
    }

    public void reLoadTestDB() {
        this.databaseSeederService.resetDB();
    }

    public TokenOutputDto getTokenDto() {
        return tokenDto;
    }

    public void setTokenDto(TokenOutputDto tokenDto) {
        this.tokenDto = tokenDto;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

}
