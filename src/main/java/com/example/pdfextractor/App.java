package com.example.pdfextractor;

import java.io.File;
import java.util.ArrayList;
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

        if (args.length < 2) {
            System.out.println(
                    "Usage: java -jar citation-extractor.jar -i input.pdf -f(multiple options) pdf | txt | word -t (optional) title");
            return;
        }

        String inputPath = null, title = null;
        List<String> formats = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i" -> inputPath = args[++i];
                case "-f" -> {
                    while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        formats.add(args[++i]);
                    }
                }
                case "-t" -> title = args[++i];
                default -> {
                    System.out.println("Unknown argument: " + args[i]);
                    return;
                }
            }
        }

        if (inputPath == null || formats.isEmpty()) {
            System.out.println("Missing required arguments.");
            return;
        }

        File file = new File(inputPath);

        Extractor extractor = new Extractor();

        try (PDDocument document = Loader.loadPDF(file)) {
            LinkedHashMap<Integer, List<Annotation>> allAnnotations = extractor.getAnnotations(document);

            ExporterFactory exporterFactory = new ExporterFactory();

            String[] format = { "pdf", "word" };
            String exportTitle = title == null ? "Annotations" : title;

            List<IAnnotationExporter> annotationExporters = exporterFactory.getExporter(allAnnotations, format, exportTitle);
            for (IAnnotationExporter exporter : annotationExporters) {
                exporter.export();
            }

        } catch (Exception e) {
            logger.error("Error during extractor execution", e);
        }
    }
}
