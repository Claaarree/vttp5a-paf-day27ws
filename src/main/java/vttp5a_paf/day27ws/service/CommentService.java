package vttp5a_paf.day27ws.service;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import jakarta.json.Json;
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
            doc.append("rating", Integer.parseInt(review.getFirst("rating")));
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

    public JsonObject getLatestComment(String reviewId) {
        JsonObjectBuilder jObject = Json.createObjectBuilder();
        
        Optional<Document> opt = commentRepository.getComment(reviewId);
        if(opt.isEmpty()){
            jObject.add("error", "The provided comment Id does not exist!");
            return jObject.build();
        } else {
            Document doc = opt.get();
            List<Document> editedList = doc.getList("edited", Document.class, new ArrayList<>());
            if (editedList.isEmpty()) {
                doc.append("edited", false);
            } else {
                Document latestUpdate = editedList.getFirst();
                Integer rating = latestUpdate.getInteger("rating");
                String comment = latestUpdate.getString("comment");
                String posted = latestUpdate.getString("posted");

                doc.replace("rating", rating);
                doc.replace("comment", comment);
                doc.replace("posted", posted);
                doc.replace("edited", true);
            }
            doc.append("timestamp", LocalDateTime.now().toString());

            return Json.createReader(new StringReader(doc.toJson())).readObject();
        }
    }

    public JsonObject getCommentHistory(String reviewId) {
        JsonObjectBuilder jObject = Json.createObjectBuilder();

        Optional<Document> opt = commentRepository.getComment(reviewId);
        if(opt.isEmpty()){
            jObject.add("error", "The provided comment Id does not exist!");
            return jObject.build();
        } else {
            Document doc = opt.get();
            doc.append("timestamp", LocalDateTime.now().toString());
            return Json.createReader(new StringReader(doc.toJson())).readObject();
        }
    }
}
