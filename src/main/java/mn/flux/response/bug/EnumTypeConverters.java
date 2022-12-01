package mn.flux.response.bug;

import io.micronaut.context.annotation.Factory;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;
import java.util.Optional;
import mn.flux.response.bug.model.ResourceName;

@Factory
public class EnumTypeConverters {
    @Singleton
    @SuppressWarnings({"unused"})
    public TypeConverter<String, ResourceName> stringToResourceNameConverter() {
        return (String object, Class<ResourceName> targetType, ConversionContext context) -> Optional.of(ResourceName.fromValue(object));
    }
}
