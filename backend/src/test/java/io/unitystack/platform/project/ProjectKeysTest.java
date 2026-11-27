package io.unitystack.platform.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;

class ProjectKeysTest {

    @Test
    void usesInitialsForMultiWordNames() {
        assertThat(ProjectKeys.generate("GP Booking App", k -> false)).isEqualTo("GBA");
    }

    @Test
    void usesPrefixForSingleWord() {
        assertThat(ProjectKeys.generate("UnityStack", k -> false)).isEqualTo("UNIT");
    }

    @Test
    void appendsNumberWhenTaken() {
        Set<String> taken = Set.of("GBA", "GBA2");
        assertThat(ProjectKeys.generate("GP Booking App", taken::contains)).isEqualTo("GBA3");
    }

    @Test
    void handlesNamesWithoutLatinLetters() {
        String key = ProjectKeys.generate("日本", k -> false);
        assertThat(ProjectKeys.VALID.matcher(key).matches()).isTrue();
    }

    @Test
    void prefixesLeadingDigit() {
        String key = ProjectKeys.generate("3D Printer Hub", k -> false);
        assertThat(key).startsWith("P");
        assertThat(ProjectKeys.VALID.matcher(key).matches()).isTrue();
    }
}
