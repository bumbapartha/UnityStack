package io.unitystack.platform;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/** AD-5: modules use only each other's public interfaces, and there are no cycles. Do not weaken. */
class ModularityTest {

    @Test
    void moduleBoundariesAreRespected() {
        ApplicationModules.of(UnityStackApplication.class).verify();
    }
}
