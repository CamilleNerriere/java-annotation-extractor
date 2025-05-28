package com.example.pdfextractor.exporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.example.pdfextractor.model.Annotation;

public class ExporterFactory {
    public List<IAnnotationExporter> getExporter(LinkedHashMap<Integer, List<Annotation>> annotations, String[] formats,
            String title) {
        List<String> uniqueFormats = removeRedondantFormat(formats);
        return getAskedExporters(uniqueFormats, annotations);

    }

    private List<String> removeRedondantFormat(String formats[]) {
        return Arrays.stream(formats).map(String::toLowerCase).distinct().toList();
    }

    private List<IAnnotationExporter> getAskedExporters(List<String> uniqueFormats,
            LinkedHashMap<Integer, List<Annotation>> annotations) {

        List<IAnnotationExporter> exporters = new ArrayList<>();

        for (String format : uniqueFormats) {
            if (format == null)
                return null;
            IAnnotationExporter exporter = switch (format) {
                case "word" -> new WordAnnotationExporter(annotations);
                case "pdf" -> new PdfAnnotationExporter(annotations);
                default -> throw new IllegalArgumentException("Unknown format : " + format);
            };
            exporters.add(exporter);
        }
        
        return exporters;
    }

}
