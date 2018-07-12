package org.sask.chatbot.KeywordSearch;
/**
 * @author Muzammil Ahmed
 * @since 19-06-2018
 */
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

public class ReadModel {

    public static Model readModel(String fileName) throws IllegalArgumentException {

        Model model = ModelFactory.createDefaultModel();
        InputStream inputStream = FileManager.get().open(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException(fileName + " not found!!!");
        }
        if (fileName.contains(".rdf")) {
            model.read(inputStream, null);
        }else if(fileName.contains(".nt")){
            model.read(inputStream, null, "N-TRIPLE");
        } else {
            model.read(fileName);
        }
        return model;
    }
}
