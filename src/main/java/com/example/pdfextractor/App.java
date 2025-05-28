package com.example.pdfextractor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.pdfextractor.exporter.ExporterFactory;
import com.example.pdfextractor.exporter.IAnnotationExporter;
import com.example.pdfextractor.extractor.Extractor;
import com.example.pdfextractor.model.Annotation;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        File file = new File("pdf-annotation-extractor/src/main/resources/test.pdf");
        Extractor extractor = new Extractor();
        try (PDDocument document = Loader.loadPDF(file)) {
            LinkedHashMap<Integer, List<Annotation>> allAnnotations = extractor.getAnnotations(document);

            ExporterFactory exporterFactory = new ExporterFactory();
            
            String[] format = {"pdf", "word"};
            String title = "Simondon - ILFI";

            List<IAnnotationExporter> annotationExporters = exporterFactory.getExporter(allAnnotations, format, title);
            for(IAnnotationExporter exporter : annotationExporters){
                exporter.export();
            }  

        } catch (Exception e) {
            logger.error("Error during extractor execution", e);
        }
    }
}
