package io.unitystack.platform.requirements.api;

/** FR-107 settings change; null fields are left unchanged. */
public record UpdateProjectRequest(String name, String key, String status) {}
