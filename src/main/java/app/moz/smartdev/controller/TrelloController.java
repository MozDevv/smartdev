package app.moz.smartdev.controller;

import app.moz.smartdev.service.OauthTokenService;
import app.moz.smartdev.service.TrelloService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@AllArgsConstructor
@RestController
public class TrelloController {

    private final OauthTokenService oauthTokenService;
    private final TrelloService trelloService;

    @GetMapping("/api/trello/callback")
    public ResponseEntity<String> handleTrelloCallback(@RequestParam("token") String token
    ) {
        // Log the received parameters
        System.out.println("Received Trello callback with token: " + token);


        // For now, just return a simple response
        return ResponseEntity.ok("Trello callback received successfully");
    }

    @GetMapping("/api/trello/callback/save")
    public ResponseEntity<String> handleTrelloCallback2(@RequestParam("token") String token, HttpServletRequest request) {
        try {
            oauthTokenService.saveOAuthToken(
                    token,
                    "Trello",
                    request
            );
            return ResponseEntity.ok("Trello token saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving Trello token: " + e.getMessage());
        }
    }

    @GetMapping("/api/trello/getTrelloData")
    public ResponseEntity<Map<String, Object> >getTrelloData(HttpServletRequest request) {
        try{
            Map<String, Object> data= trelloService.fetchAllTrelloData(request);

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Error fetching Trello data: " + e.getMessage())
            );
        }
    }

}