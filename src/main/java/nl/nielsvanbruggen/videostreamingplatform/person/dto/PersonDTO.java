package nl.nielsvanbruggen.videostreamingplatform.person.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonDTO {
        private String firstname;
        private String lastname;
}
