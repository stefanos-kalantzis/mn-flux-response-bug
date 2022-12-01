package mn.flux.response.bug.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.core.annotation.Introspected;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Introspected
public enum ResourceName {
    ARTICLE("articles"),
    VIDEO("videos");

    private final String jsonValue;

    @JsonCreator
    public static ResourceName fromValue(String value) {
        for (ResourceName contentType : ResourceName.values()) {
            if (contentType.getJsonValue().equalsIgnoreCase(value)) {
                return contentType;
            }
        }
        throw new IllegalArgumentException("Unknown ResourceName value");
    }

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }
}
