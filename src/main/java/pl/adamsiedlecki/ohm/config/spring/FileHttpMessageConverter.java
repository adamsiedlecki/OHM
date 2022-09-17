package pl.adamsiedlecki.ohm.config.spring;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.ohm.config.OhmConfigProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Component
public class FileHttpMessageConverter extends AbstractHttpMessageConverter<File> {

    private final OhmConfigProperties ohmConfigProperties;

    public FileHttpMessageConverter(OhmConfigProperties ohmConfigProperties) {
        super(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG, MediaType.IMAGE_GIF);
        this.ohmConfigProperties = ohmConfigProperties;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return File.class.isAssignableFrom(clazz);
    }

    @Override
    protected File readInternal(Class<? extends File> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return Files.write(new File(ohmConfigProperties.getImageStoragePath() + UUID.randomUUID() +".jpg").toPath(), inputMessage.getBody().readAllBytes()).toFile();
    }

    @Override
    protected void writeInternal(File file, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getBody().write(Files.readAllBytes(file.toPath()));
        outputMessage.getBody().flush();
    }
}
