package nl.nielsvanbruggen.videostreamingplatform.global.service;

import com.sun.jdi.InternalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ImageService {
    @Value("${env.thumbnail.width}")
    private String imageWidth;
    @Value("${env.thumbnail.height}")
    private String imageHeight;
    @Value("${env.thumbnail.root}")
    private String rootPath;

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
        final int width = Integer.parseInt(imageWidth);
        final int height = Integer.parseInt(imageHeight);
        final BufferedImage convertedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Image img = bufferedImage.getScaledInstance(-1, height, Image.SCALE_DEFAULT);
        final int off = (img.getWidth(null) - width) / 2;
        convertedImage.createGraphics().drawImage(img, -off, 0, Color.WHITE,null);

        return convertedImage;
    }

    private void writeImageAsJpg(BufferedImage bufferedImage, String name) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(rootPath + "/" + name);
        final boolean canWrite = ImageIO.write(bufferedImage, "jpg", fileOutputStream);
        fileOutputStream.close();

        if (!canWrite) {
            throw new IOException("Failed to write image.");
        }
    }
}
