package com.iogetArray.server;

import com.iogetArray.server.enumeration.Status;
import com.iogetArray.server.models.Server;
import com.iogetArray.server.serverRepository.ServerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	/*@Bean
	CommandLineRunner run(ServerRepository serverRepository){
		return args -> {
			serverRepository.save(new Server(null,"192.168.1.160", "Unbuntu Linux",
					"16 GB", "Personnal PC", "http://localhost:8080/server/image/server1.png", Status.SERVER_UP));
			serverRepository.save(new Server(null,"192.168.1.58", "Fedora Linux",
					"32 GB", "Dell Tower", "http://localhost:8080/server/image/server2.png", Status.SERVER_DOWN));
			serverRepository.save(new Server(null,"192.168.1.21", "MS 2008",
					"64 GB", "Web Server", "http://localhost:8080/server/image/server3.png", Status.SERVER_UP));
			serverRepository.save(new Server(null,"192.168.1.14", "Red Hat Enterprise Linux",
					"60 GB", "Mail Server", "http://localhost:8080/server/image/server4.png", Status.SERVER_DOWN));
		};

	}*/

	@Bean
	public CorsFilter corsFilter(){
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin","Access-Control-Allow-Origin", "Content-Type",
				"Accept","Authorization","Origin","Accept","X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin","Content-Type","Accept","Authorization",
				"Access-Control-Allow-Origin","Access-Control-Allow-Origin","Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
