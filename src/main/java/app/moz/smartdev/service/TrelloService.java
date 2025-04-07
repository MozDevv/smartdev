package app.moz.smartdev.service;

import app.moz.smartdev.configs.JwtService;
import app.moz.smartdev.entity.*;
import app.moz.smartdev.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class TrelloService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final BoardRepository boardRepository;
    private final ListRepository listRepository;
    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private final AuthProviderRepository authProviderRepository;
    private final OauthTokenRepository oauthTokenRepository;

    public Map<String, Object> fetchAllTrelloData(HttpServletRequest request) {
        User user = jwtService.getUserFromRequest(request);
        AuthProvider authProvider = authProviderRepository.findByProviderName("Trello");
        OauthToken oauthToken2 = oauthTokenRepository.findOauthTokenByUserAndProvider(user, authProvider);

        if (oauthToken2 == null) {
            throw new RuntimeException("Oauth token not found");
        }
        String oauthToken = oauthToken2.getAccessToken();
        String apiKey = authProvider.getClientId();

        log.info("Fetching Trello Data 👽👽👽👽👿");
        log.info("API Key: 🐣 {}", apiKey);
        log.info("Oauth Token: 🍥{}", oauthToken);

        fetchTrelloBoards(apiKey, oauthToken, user);
        List<TrelloBoard> boards = boardRepository.findByUser(user);
        Map<String, TrelloList> listMap = new HashMap<>();
        Map<String, TrelloCard> cardMap = new HashMap<>();
        Map<String, List<TrelloComment>> commentMap = new HashMap<>();

        for (TrelloBoard board : boards) {
            fetchTrelloLists(apiKey, oauthToken, board.getBoardId(), listMap);
        }

        for (TrelloList list : listMap.values()) {
            fetchTrelloCards(apiKey, oauthToken, list.getListId(), cardMap);
        }

        for (TrelloCard card : cardMap.values()) {
            fetchTrelloComments(apiKey, oauthToken, card.getCardId(), user);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("boards", boards);

        return result;
    }

    public void fetchTrelloBoards(String apiKey, String oauthToken, User user) {
        String url = String.format("https://api.trello.com/1/members/me/boards?key=%s&token=%s", apiKey, oauthToken);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getBody() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                List<TrelloBoard> boards = new ArrayList<>();
                for (JsonNode boardNode : rootNode) {
                    String boardId = boardNode.get("id").asText();
                    String name = boardNode.get("name").asText();

                    // Check if the board already exists by boardId or name
                    if (!boardRepository.existsByBoardIdOrName(boardId, name)) {
                        TrelloBoard board = new TrelloBoard();
                        board.setBoardId(boardId);
                        board.setName(name);
                        board.setUser(user);
                        boards.add(board);
                    }
                }
                boardRepository.saveAll(boards);
            } catch (Exception e) {
                log.error("Error parsing Trello boards response: {}", e.getMessage());
            }
        }
    }

    public void fetchTrelloLists(String apiKey, String oauthToken, String boardId, Map<String, TrelloList> listMap) {
        String url = String.format("https://api.trello.com/1/boards/%s/lists?key=%s&token=%s", boardId, apiKey, oauthToken);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getBody() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                List<TrelloList> lists = new ArrayList<>();
                for (JsonNode listNode : rootNode) {
                    String listId = listNode.get("id").asText();
                    String name = listNode.get("name").asText();
                    // Check if the list already exists by listId or name
                    if (!listRepository.existsByListIdOrName(listId, name)) {
                        TrelloBoard board = boardRepository.findByBoardId(boardId);
                        TrelloList list = new TrelloList();
                        list.setListId(listId);
                        list.setName(name);
                        list.setBoard(board);
                        lists.add(list);
                        listMap.put(list.getListId(), list);
                    }
                }
                listRepository.saveAll(lists);
            } catch (Exception e) {
                log.error("Error parsing Trello lists response: {}", e.getMessage());
            }
        }
    }

    public void fetchTrelloCards(String apiKey, String oauthToken, String listId, Map<String, TrelloCard> cardMap) {
        String url = String.format("https://api.trello.com/1/lists/%s/cards?key=%s&token=%s", listId, apiKey, oauthToken);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getBody() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                List<TrelloCard> cards = new ArrayList<>();
                for (JsonNode cardNode : rootNode) {
                    TrelloCard card = new TrelloCard();
                    TrelloList list = listRepository.findByListId(listId);
                    card.setCardId(cardNode.get("id").asText());
                    card.setName(cardNode.get("name").asText());
                    card.setDescription(cardNode.get("desc").asText());

                    if (cardNode.hasNonNull("due")) {
                        card.setDueDate(Timestamp.from(OffsetDateTime.parse(cardNode.get("due").asText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant()));
                    } else {
                        card.setDueDate(null);
                    }
                    card.setTrelloList(list);
                    cards.add(card);
                    cardMap.put(card.getCardId(), card);
                }
                cardRepository.saveAll(cards);
            } catch (Exception e) {
                log.error("Error parsing Trello cards response: {}", e.getMessage());
            }
        }
    }

    public void fetchTrelloComments(String apiKey, String oauthToken, String cardId, User user) {
        String url = String.format("https://api.trello.com/1/cards/%s/actions?filter=commentCard&key=%s&token=%s", cardId, apiKey, oauthToken);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getBody() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                List<TrelloComment> comments = new ArrayList<>();
                for (JsonNode commentNode : rootNode) {
                    TrelloComment comment = new TrelloComment();
                    TrelloCard card = cardRepository.findByCardId(cardId);
                    comment.setCommentId(commentNode.get("id").asText());
                    comment.setBody(commentNode.get("data").get("text").asText());
                    comment.setCard(card);
                    comment.setUser(user);
                    comments.add(comment);
                }
                commentRepository.saveAll(comments);
            } catch (Exception e) {
                log.error("Error parsing Trello comments response: {}", e.getMessage());
            }
        }
    }
}