package eam.parcial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ParcialApplication {

	@RequestMapping("/")
	public String home(){
		return "Hola desde mi parcial de Spring Boot con Docker!";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ParcialApplication.class, args);
	}

}
