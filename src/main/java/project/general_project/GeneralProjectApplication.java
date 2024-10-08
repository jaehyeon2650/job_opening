package project.general_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class GeneralProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneralProjectApplication.class, args);
    }

}
