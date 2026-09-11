package martintripodi.JCoin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Mapper {
    public static ObjectMapper createMapper() {

        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();

        module.addSerializer(
                PublicKey.class,
                new JsonSerializer<PublicKey>() {

                    @Override
                    public void serialize(
                            PublicKey key,
                            JsonGenerator gen,
                            SerializerProvider serializers)
                            throws IOException {

                        gen.writeString(
                                Base64.getEncoder()
                                        .encodeToString(key.getEncoded())
                        );
                    }
                }
        );

        module.addDeserializer(
                PublicKey.class,
                new JsonDeserializer<PublicKey>() {

                    @Override
                    public PublicKey deserialize(
                            JsonParser parser,
                            DeserializationContext context)
                            throws IOException {

                        try {
                            byte[] bytes =
                                    Base64.getDecoder()
                                            .decode(parser.getText());

                            X509EncodedKeySpec spec =
                                    new X509EncodedKeySpec(bytes);

                            KeyFactory factory =
                                    KeyFactory.getInstance("Ed25519");

                            return factory.generatePublic(spec);

                        } catch (Exception e) {
                            throw new IOException(
                                    "Could not deserialize public key",
                                    e
                            );
                        }
                    }
                }
        );

        mapper.registerModule(module);

        return mapper;
    }
}
