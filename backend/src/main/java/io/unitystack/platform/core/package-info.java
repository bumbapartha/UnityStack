/**
 * Shared platform code used by every feature module (AD-5). Core depends on no feature module. Other
 * modules may use only the named interfaces: {@code core.ai}, {@code core.common} and {@code core.persistence}.
 */
@ApplicationModule(displayName = "Core")
package io.unitystack.platform.core;

import org.springframework.modulith.ApplicationModule;
