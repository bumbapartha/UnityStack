package io.unitystack.platform.requirements.domain;

/** One turn of the inception conversation. role is "user" or "assistant". */
public record ChatMessage(String role, String content) {}
