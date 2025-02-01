package vttp5a_paf.day27ws.service;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp5a_paf.day27ws.repository.CommentRepository;
import vttp5a_paf.day27ws.repository.GameRepository;

@Service
public class CommentService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CommentRepository commentRepository;
    
    public JsonObject handleComment(MultiValueMap<String, String> review){
        JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();

        Integer gameId = Integer.parseInt(review.getFirst("game id"));
        String gameName = gameRepository.getGameName(gameId);
        if (gameName.contentEquals("null")){
            jObjectBuilder.add("error", "The provided game ID does not exist!")
                    .build();
        } else {
            Document doc = new Document();
            doc.append("user", review.getFirst("name"));
            doc.append("rating", review.getFirst("rating"));
            doc.append("comment", review.getFirst("comment"));
            doc.append("ID", gameId);
            doc.append("posted", LocalDate.now().toString());
            doc.append("name", gameName);
            String commentId = commentRepository.insertComment(doc);

            jObjectBuilder.add("success", String.format("Your comment ID is: %s", commentId));
        }

        return jObjectBuilder.build();
    }

    public JsonObject updateComment(String commentId, String jsonUpdate) {
        JsonObjectBuilder jObject = Json.createObjectBuilder();

        Document updateEdit = Document.parse(jsonUpdate);
        updateEdit.append("posted", LocalDateTime.now().toString());
        
        if (commentRepository.updateComment(commentId, updateEdit) == 0){
            jObject.add("error", "The provided comment Id does not exist!");
        } else {
            jObject.add("success", "The comment has been updated!");
        }

        return jObject.build();
    }
}
