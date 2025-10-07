import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.mycompany")
public class VeterinariaApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(VeterinariaApplication.class, args);
    }
}
 
