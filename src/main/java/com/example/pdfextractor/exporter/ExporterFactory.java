package com.example.pdfextractor.exporter;

import java.util.LinkedHashMap;
import java.util.List;

import com.example.pdfextractor.model.Annotation;

public class ExporterFactory {
    public IAnnotationExporter getExporter(LinkedHashMap<Integer, List<Annotation>> annotations, String format){
        return switch(format.toLowerCase()){
            case "word" -> new WordAnnotationExporter(annotations);
            case "pdf" -> new PdfAnnotationExporter(annotations);
            default -> throw new IllegalArgumentException("Unknown format : " + format);
        };

    }
}
