package cc.uncarbon;

import cc.uncarbon.framework.crud.annotation.EnableInitHikariPoolAtStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author Uncarbon
 */
@EnableInitHikariPoolAtStartup
@SpringBootApplication
public class HelioBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelioBootApplication.class, args);
    }

}
