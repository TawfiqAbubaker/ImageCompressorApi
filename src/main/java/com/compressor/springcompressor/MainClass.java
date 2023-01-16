package com.compressor.springcompressor;

import com.aspose.imaging.Image;
import com.aspose.imaging.fileformats.jpeg.JpegCompressionMode;
import com.aspose.imaging.imageoptions.JpegOptions;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

@RestController
@CrossOrigin
public class MainClass {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String getGreeting() {
        return "Hello fellow human being!";
    }
    @PostMapping("/compress")
    @ResponseBody
    public  ResponseEntity<InputStreamResource> compressImage(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws IOException {
        System.out.println("the type provided is " + type);
        String name = "";
        name = type.equals("true")? "YOUR_COMPRESSED_IMAGE.jpg" : "YOUR_COMPRESSED_IMAGE.png";
        File compressedImageFile = new File(name);
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = new FileOutputStream(compressedImageFile);

        float imageQuality = 0.1f;

        //Create the buffered image
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        //Get image writers
        String extension = "";
        extension = type.equals("true")? "jpg" : "png";
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(extension);

        if (!imageWriters.hasNext())
            throw new IllegalStateException("Writers Not Found!!");

        ImageWriter imageWriter = (ImageWriter) imageWriters.next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        imageWriter.setOutput(imageOutputStream);

        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        //Set the compress quality metrics
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(imageQuality);

        //Created image
        imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

        // close all streams
        inputStream.close();
        outputStream.close();
        imageOutputStream.close();
        imageWriter.dispose();

        File compressedImageFile2 = new File(name);
        MediaType contentType = MediaType.IMAGE_JPEG;
        FileInputStream in = new FileInputStream(compressedImageFile2);
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }
    //    public pngCompressor()
    @GetMapping("/get-image")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImageDynamicType(@RequestParam("jpg") boolean jpg) throws FileNotFoundException {
        MediaType contentType = jpg ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
        System.out.println("the type provided is " + jpg);
        String name = jpg ? "YOUR_COMPRESSED_IMAGE.jpg" : "YOUR_COMPRESSED_IMAGE.png";
        File compressedImageFile = new File(name);

        FileInputStream in = new FileInputStream(compressedImageFile);
        InputStreamResource test = new InputStreamResource(in);
        compressedImageFile.delete();
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(test);
    }
    @DeleteMapping("/image")
    public String deleteImage(@RequestParam("jpg") boolean jpg) {
        String name = jpg ? "YOUR_COMPRESSED_IMAGE.jpg" : "YOUR_COMPRESSED_IMAGE.png";
        File compressedImageFile = new File(name);
        boolean verdict = compressedImageFile.delete();
        return (verdict? "Done" : "Failed");
    }
}
