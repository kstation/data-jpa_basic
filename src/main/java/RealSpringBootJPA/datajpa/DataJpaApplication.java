package RealSpringBootJPA.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider(){
		//실제로는 세션 정보를 리턴 시켜야하나 임의로  uuid를 발급
		return () -> Optional.of(UUID.randomUUID().toString());
		//return new AuditorAware<String>() {
		//			@Override
		//			public Optional<String> getCurrentAuditor() {
		//					return Optional.of(UUID.randomUUID().toString());
		//			}
		//		};
	}

}
