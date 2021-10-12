package ch.stefanjucker.videoexpertise.service.basketplan;

import ch.stefanjucker.videoexpertise.dto.basketplan.BasketplanGame;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;

import static javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING;

@Service
public class BasketplanService {

    private static final String SEARCH_GAMES_URL = "https://www.basketplan.ch/showSearchGames.do?actionType=searchGames&gameNumber=%s&xmlView=true&perspective=de_default&federationId=%d";


    public Optional<BasketplanGame> findGameByNumber(Federation federation, String gameNumber) throws IOException, InterruptedException {
        // TODO validate arguments

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setFeature(FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(SEARCH_GAMES_URL.formatted(gameNumber, federation.getId()));
            doc.getDocumentElement().normalize();

            NodeList games = doc.getDocumentElement().getElementsByTagName("game");
            if (games.getLength() == 1) {
                Node game = games.item(0);
                NamedNodeMap attributes = game.getAttributes();

                return Optional.of(new BasketplanGame(attributes.getNamedItem("gameNumber").getNodeValue(),
                                                      attributes.getNamedItem("referee1Name").getNodeValue(),
                                                      attributes.getNamedItem("referee2Name").getNodeValue(),
                                                      attributes.getNamedItem("referee3Name").getNodeValue()));
            } else {
                // TODO log more than one found
            }

        } catch (ParserConfigurationException | SAXException e) {
            // TODO log it

        }

        return Optional.empty();
    }


    public enum Federation {
        SBL(12),
        PROBASKET(10);

        private final int id;

        Federation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }


}
