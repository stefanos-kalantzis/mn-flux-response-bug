package mn.flux.response.bug.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PublishStatus {
    DRAFT("draft"),
    PUBLISHED("published");

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
