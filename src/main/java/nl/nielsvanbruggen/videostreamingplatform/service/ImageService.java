package nl.nielsvanbruggen.videostreamingplatform.service;

import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.config.PathProperties;
import nl.nielsvanbruggen.videostreamingplatform.config.SnapshotProperties;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ImageService {
    private static final Pattern base64ImagePattern = Pattern.compile("data:image/(.+);base64");

    private final SnapshotProperties snapshotProperties;
    private final PathProperties pathProperties;

    public void saveImage(String base64Image, String imageName) throws IOException {
        String[] parts = base64Image.split(",");
        Matcher matcher = base64ImagePattern.matcher(parts[0]);

        if(parts.length < 2 || !matcher.find()) throw new IOException("Base 64 image is not formatted correctly");

        InputStream is = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(parts[1]));

        this.saveImage(is, imageName);
    }

    public void saveImage(InputStream imageStream, String imageName) throws IOException {
        try {
            BufferedImage image = ImageIO.read(imageStream);
            BufferedImage resizedImage = cropImage(image);
            writeImageAsJpg(resizedImage, imageName);
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }
    private BufferedImage cropImage(BufferedImage bufferedImage) {
        int w = snapshotProperties.getWidth();
        int h = snapshotProperties.getHeight();
        BufferedImage convertedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        //Checks aspect ratio.
        if(((double) bufferedImage.getWidth() / bufferedImage.getHeight()) > ((double) w / h)) {
            Image img = bufferedImage.getScaledInstance(-1, h, Image.SCALE_DEFAULT);
            final int off = (img.getWidth(null) - w) / 2;
            convertedImage.createGraphics().drawImage(img, -off, 0, Color.WHITE,null);

            return convertedImage;
        }

        Image img = bufferedImage.getScaledInstance(w, -1, Image.SCALE_DEFAULT);
        int off = (img.getHeight(null) - h) / 2;
        convertedImage.createGraphics().drawImage(img, 0, -off, Color.WHITE,null);

        return convertedImage;
    }

    private void writeImageAsJpg(BufferedImage bufferedImage, String name) throws IOException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(Path.of(pathProperties.getThumbnail().getRoot(), name).toFile())) {
            boolean canWrite = ImageIO.write(bufferedImage, "jpg", fileOutputStream);

            if (!canWrite) {
                throw new IOException("Failed to write image.");
            }
        }
    }
}
