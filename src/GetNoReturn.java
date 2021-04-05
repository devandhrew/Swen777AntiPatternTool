import org.w3c.dom.*;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetNoReturn {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse("springframework.xml");
            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();

            // Create XPath object
            XPath xpath = xpathFactory.newXPath();

            List<String> nodes = getNoReturn(doc, xpath);
            System.out.println(nodes.size());
            System.out.println(Arrays.toString(nodes.toArray()));
            

        } catch (Exception e) {
            e.printStackTrace();  
        }
        
    }

    private static List<String> getNoReturn(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr = xpath.compile("/unit//unit/class/block/function[type/name='void'][starts-with(./name,substring('get',1,3)) or starts-with(./name,substring('return',1,6))]/name");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getTextContent());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }
}
