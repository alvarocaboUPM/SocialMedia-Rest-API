package sos.rest.services;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import sos.rest.models.*;
public class XmlToUserConverter {

    public static User fromXml(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        User user = (User) unmarshaller.unmarshal(reader);
        return user;
    }
}

