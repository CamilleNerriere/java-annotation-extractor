package com.example.pdfextractor;

import java.io.File;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.pdfextractor.extractor.Extractor;


public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        File file = new File("pdf-annotation-extractor/src/resources/test.pdf");
        Extractor extractor = new Extractor();
        try(PDDocument document = Loader.loadPDF(file)){
            extractor.getAnnotations(document);

        } catch (Exception e) {
            logger.error("Error during extractor execution", e);
        }
    }
}
