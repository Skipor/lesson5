package ru.skipor.RssReader.RSSFeedReader;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Vladimir Skipor on 10/17/13.
 * Email: vladimirskipor@gmail.com
 */
public class DOMRSSReader extends BaseRSSFeedReader {
    public DOMRSSReader(String feedUrl) throws RSSFeedReaderException {
        super(feedUrl);
    }

    public List<RSSItem> parse() throws RSSFeedReaderException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<RSSItem> rssItems = new ArrayList<RSSItem>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(this.getInputStream());
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName(ITEM);
            for (int i = 0; i < items.getLength(); i++) {
                RSSItem item = new RSSItem();
                Node node = items.item(i);
                NodeList properties = node.getChildNodes();
                for (int j = 0; j < properties.getLength(); j++) {
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase(TITLE)) {
                        item.setTitle(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase(LINK)) {
                        item.setLink(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                        StringBuilder text = new StringBuilder();
                        NodeList chars = property.getChildNodes();
                        for (int k = 0; k < chars.getLength(); k++) {
                            text.append(chars.item(k).getNodeValue());
                        }
                        item.setDescription(text.toString());
                    } else if (name.equalsIgnoreCase(PUB_DATE)) {
                        item.setDate(property.getFirstChild().getNodeValue());
                    }
                }
                rssItems.add(item);

            }

        } catch (ParserConfigurationException e) {
            throw new RSSFeedReaderException(e);
        } catch (SAXException e) {
            throw new RSSFeedReaderException(e);
        } catch (IOException e) {
            throw new RSSFeedReaderException(e);
        }
        return rssItems;
    }
}

