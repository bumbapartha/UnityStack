package io.unitystack.platform.requirements.domain;

import java.util.List;

/** Renders a charter as Markdown; this is the form later proposed to Git (FR-108). */
public final class CharterMarkdown {
    private CharterMarkdown() {}

    public static String render(Charter c, String projectKey, List<String> openQuestions) {
        StringBuilder md = new StringBuilder();
        md.append("# ").append(orTbd(c.name())).append(" — Project Charter\n\n");
        if (projectKey != null)
            md.append("**Project key:** ").append(projectKey).append("\n\n");
        section(md, "Problem", c.problem());
        section(md, "Target users", c.targetUsers());
        String type =
                c.productType() == null ? null : Charter.PRODUCT_TYPES.getOrDefault(c.productType(), c.productType());
        if (type != null && c.productTypeDetails() != null && !type.equalsIgnoreCase(c.productTypeDetails())) {
            type = type + " — " + c.productTypeDetails();
        }
        section(md, "Product type", type);
        section(md, "Expected benefit", c.expectedBenefit());
        section(md, "Scale", c.scale() == null ? null : Charter.SCALES.getOrDefault(c.scale(), c.scale()));
        if (c.motivation() != null) section(md, "Why now", c.motivation());
        if (c.constraints() != null) section(md, "Constraints", c.constraints());
        if (c.outOfScope() != null) section(md, "Out of scope", c.outOfScope());
        if (openQuestions != null && !openQuestions.isEmpty()) {
            md.append("## Open questions\n\n");
            openQuestions.forEach(q -> md.append("- ").append(q).append('\n'));
            md.append('\n');
        }
        return md.toString();
    }

    private static void section(StringBuilder md, String title, String value) {
        md.append("## ").append(title).append("\n\n").append(orTbd(value)).append("\n\n");
    }

    private static String orTbd(String v) {
        return v == null ? "_To be defined_" : v;
    }
}
