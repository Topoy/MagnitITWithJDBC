import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class XMLParser {
    public static void createXML(List<Integer> fields, String path) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = dbf.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement("entries");
            Element entryElement = document.createElement("entry");

            document.appendChild(rootElement);
            rootElement.appendChild(entryElement);

            for (Integer field : fields) {
                Element fieldElement = document.createElement("field");
                fieldElement.setTextContent(field + "");
                entryElement.appendChild(fieldElement);
            }

            StreamResult file = new StreamResult(new File(path));
            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, file);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void transformXML(String xsltPath, String xmlPath, String resultPath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File(xsltPath));
            Transformer transformer = transformerFactory.newTransformer(xslt);
            Source xml = new StreamSource(new File(xmlPath));
            transformer.transform(xml, new StreamResult(new File(resultPath)));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Long getFieldValuesSum(String xmlPath) {
        long result = 0L;
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(xmlPath));

            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String value = reader.getAttributeValue(0);
                    if (value != null && !value.isEmpty()) {
                        result += Long.parseLong(value);
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
