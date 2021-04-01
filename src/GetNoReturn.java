import org.w3c.dom.*;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;



import java.util.List;

public class GetNoReturn {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse("test.xml");
            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();

            // Create XPath object
            XPath xpath = xpathFactory.newXPath();
            
            XPathExpression expr = xpath.compile("//class/block/function/name");
            Object result = expr.evaluate(doc, XPathConstants.STRING);
            System.out.println(result);
            

        } catch (Exception e) {
            e.printStackTrace();  
        }
        
    }
}
