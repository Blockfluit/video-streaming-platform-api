package nl.nielsvanbruggen.videostreamingplatform;

import nl.nielsvanbruggen.videostreamingplatform.invitetoken.InviteToken;
import nl.nielsvanbruggen.videostreamingplatform.invitetoken.InviteTokenRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.global.util.TokenGeneratorUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
public class VideoStreamingPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoStreamingPlatformApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(InviteTokenRepository inviteTokenRepository) {
		return (args) -> {
			// TODO: Make sure the master token is generated only once
			if(!inviteTokenRepository.findAllByCreatedBy(null).isEmpty()) {
				return;
			}
			InviteToken masterToken = InviteToken.builder()
					.token(TokenGeneratorUtil.generate(64))
					.createdAt(Instant.now())
					.expiration(Instant.now().plus(365, ChronoUnit.DAYS))
					.role(Role.ADMIN)
					.build();
			inviteTokenRepository.save(masterToken);
			System.out.println(">----------------------<");
			System.out.println("Master token:");
			System.out.println(masterToken.getToken());
			System.out.println(">----------------------<");
		};
	}

}
