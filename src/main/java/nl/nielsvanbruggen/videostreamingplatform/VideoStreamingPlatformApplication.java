package nl.nielsvanbruggen.videostreamingplatform;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.invitetoken.InviteToken;
import nl.nielsvanbruggen.videostreamingplatform.invitetoken.InviteTokenRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.global.util.TokenGeneratorUtil;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
@RequiredArgsConstructor
public class VideoStreamingPlatformApplication {
	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(VideoStreamingPlatformApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(InviteTokenRepository inviteTokenRepository) {
		return (args) -> {
			if(!userRepository.findAll().isEmpty() ||
					!inviteTokenRepository.findAllByCreatedBy(null).isEmpty()) {
				return;
			}
			InviteToken masterToken = InviteToken.builder()
					.token(TokenGeneratorUtil.generate(64))
					.createdAt(Instant.now())
					.used(false)
					.master(true)
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
