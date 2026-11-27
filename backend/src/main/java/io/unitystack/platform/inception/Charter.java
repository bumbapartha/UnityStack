package io.unitystack.platform.inception;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Very high-level project intent captured during inception (Requirements.md §22.2). */
public record Charter(
        String name,
        String problem,
        String targetUsers,
        String productType,
        String productTypeDetails,
        String expectedBenefit,
        String scale,
        String motivation,
        String constraints,
        String outOfScope) {

    public static final Map<String, String> PRODUCT_TYPES = ordered(
            "website", "Website / web app",
            "mobile_app", "Mobile app",
            "api_backend", "API / backend service",
            "desktop_app", "Desktop app",
            "internal_tool", "Internal tool",
            "data_ai", "Data / AI product",
            "other", "Other");

    public static final Map<String, String> SCALES = ordered(
            "personal", "Personal",
            "team", "One team",
            "department", "One department",
            "organisation", "Whole organisation",
            "public", "Public / external customers");

    public static final List<String> MANDATORY =
            List.of("name", "problem", "targetUsers", "productType", "expectedBenefit", "scale");

    /** All captured items in the order they are asked (productTypeDetails is a sub-item of productType). */
    public static final List<String> FIELDS =
            List.of("name", "problem", "targetUsers", "productType", "expectedBenefit", "scale",
                    "motivation", "constraints", "outOfScope");

    public static Charter empty() {
        return new Charter(null, null, null, null, null, null, null, null, null, null);
    }

    /** Trims values, turns blanks into null and constrains enumerated fields to their known values. */
    public Charter normalised() {
        String type = clean(productType);
        String details = clean(productTypeDetails);
        if (type != null && !PRODUCT_TYPES.containsKey(type)) {
            details = details == null ? type : details;
            type = "other";
        }
        String sc = clean(scale);
        if (sc != null && !SCALES.containsKey(sc)) sc = null;
        return new Charter(limit(clean(name), 200), clean(problem), clean(targetUsers), type, details,
                clean(expectedBenefit), sc, clean(motivation), clean(constraints), clean(outOfScope));
    }

    public List<String> missingMandatory() {
        List<String> missing = new ArrayList<>();
        for (String f : MANDATORY) {
            if (clean(get(f)) == null) missing.add(f);
        }
        return missing;
    }

    public boolean isReady() {
        return missingMandatory().isEmpty();
    }

    public String get(String field) {
        return switch (field) {
            case "name" -> name;
            case "problem" -> problem;
            case "targetUsers" -> targetUsers;
            case "productType" -> productType;
            case "productTypeDetails" -> productTypeDetails;
            case "expectedBenefit" -> expectedBenefit;
            case "scale" -> scale;
            case "motivation" -> motivation;
            case "constraints" -> constraints;
            case "outOfScope" -> outOfScope;
            default -> throw new IllegalArgumentException("Unknown charter field: " + field);
        };
    }

    /** Returns a copy with one field replaced. */
    public Charter with(String field, String value) {
        return new Charter(
                field.equals("name") ? value : name,
                field.equals("problem") ? value : problem,
                field.equals("targetUsers") ? value : targetUsers,
                field.equals("productType") ? value : productType,
                field.equals("productTypeDetails") ? value : productTypeDetails,
                field.equals("expectedBenefit") ? value : expectedBenefit,
                field.equals("scale") ? value : scale,
                field.equals("motivation") ? value : motivation,
                field.equals("constraints") ? value : constraints,
                field.equals("outOfScope") ? value : outOfScope);
    }

    public Charter withName(String newName) {
        return with("name", newName);
    }

    static String clean(String s) {
        if (s == null) return null;
        String t = s.strip();
        return t.isEmpty() ? null : t;
    }

    private static String limit(String s, int max) {
        return s == null || s.length() <= max ? s : s.substring(0, max);
    }

    private static Map<String, String> ordered(String... kv) {
        Map<String, String> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) m.put(kv[i], kv[i + 1]);
        return java.util.Collections.unmodifiableMap(m);
    }
}
