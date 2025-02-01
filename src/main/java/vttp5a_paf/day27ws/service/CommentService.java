package vttp5a_paf.day27ws.service;

import java.time.LocalDate;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp5a_paf.day27ws.repository.GameRepository;

@Service
public class CommentService {

    @Autowired
    private GameRepository gameRepository;
    
    public void handleComment(MultiValueMap<String, String> review){

        Integer gameId = Integer.parseInt(review.getFirst("game id"));
        String gameName = gameRepository.getGameName(gameId);
        if (gameName.contentEquals("null")){
            JsonObject error = Json.createObjectBuilder()
                    .add("error", "The provided game ID does not exist!")
                    .build();
        } else {
            Document doc = new Document();
            doc.append("user", review.getFirst("name"));
            doc.append("rating", review.getFirst("rating"));
            doc.append("comment", review.getFirst("comment"));
            doc.append("ID", gameId);
            doc.append("posted", LocalDate.now().toString());
            doc.append("name", gameName);

        }
    }
}
