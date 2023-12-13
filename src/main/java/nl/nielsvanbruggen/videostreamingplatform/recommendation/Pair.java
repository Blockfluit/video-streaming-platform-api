package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pair<T, U> {
    T key;
    U value;
}
