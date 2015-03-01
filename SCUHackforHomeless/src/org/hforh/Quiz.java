package org.hforh;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;



public class Quiz {
    private List<Question> questions = new ArrayList<Question>();
    private String id;
    private String url;
    private String title;
    private String datetime;
    Question question;
    
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
    
    public void readFromXml(InputStream in) throws IOException {
        class QuestionHandler extends BasicHandler {
            @Override
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) throws SAXException {
                super.startElement(uri, localName, qName, attributes); 
            }
            @Override
            public void endElement(String uri, String localName, String qName)
                    throws SAXException {
                
                if (qName.equals("id")) id = lastString();
                else if (qName.equals("url")) url = lastString();
                else if (qName.equals("title")) title = lastString();
                else if (qName.equals("datetime")) datetime = lastString();
                else if (qName.equals("details")){
                    question = new Question(id, url, title, datetime);
                    questions.add(question);
                }
            }
        }
        
        try {       
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            //System.out.println(in);
            Log.i("immer", in+"");
            parser.parse(in, new QuestionHandler());
        } catch (Exception ex) {
            IOException ioEx = new IOException(ex.getMessage());
            ioEx.initCause(ex);
            Log.e("immer", ex.getMessage());
            throw ioEx;
        }       
    }

}
