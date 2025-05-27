package com.example.pdfextractor.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.pdfextractor.model.Annotation;

public class Extractor {
    private static final Logger logger = LoggerFactory.getLogger(Extractor.class);

    public void getAnnotations(PDDocument document) throws IOException {
        try (document) {
            int pageNum =1;
            LinkedHashMap<Integer, List<Annotation>> allAnnotations = new LinkedHashMap<>();

            for (PDPage page : document.getPages()) {
                List<Annotation> annotations = getAnnotationsPerPage(page, pageNum);

                if(annotations != null && !annotations.isEmpty()){
                    allAnnotations.put(pageNum, annotations);
                }

                pageNum++;
            }

            System.out.println(allAnnotations);
        } catch (Exception e) {
            logger.error("Error during extractor execution", e);
        }
    }

    private List<Annotation> getAnnotationsPerPage(PDPage page, int pageNum) throws IOException {
        try {
            List<PDAnnotation> annotations = page.getAnnotations();
            List<Annotation> extractedAnnotations = new ArrayList<>();
            for (PDAnnotation annotation : annotations) {
                if ("HighLight".equals(annotation.getSubtype()) || "Text".equals(annotation.getSubtype())) {
                    Annotation annot = new Annotation(pageNum, annotation.getSubtype(), annotation.getContents());
                    extractedAnnotations.add(annot);
                }
            }
            return extractedAnnotations;
        } catch (IOException e) {
            logger.error("Error during page extraction {}", pageNum, e);
        }
        return null;
    }
}
