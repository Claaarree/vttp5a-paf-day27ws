package vttp5a_paf.day27ws.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonObject;
import vttp5a_paf.day27ws.service.CommentService;

@RestController
@RequestMapping("/api")
public class CommentRestController {

    @Autowired
    private CommentService commentService;
    
    @PostMapping(path = "/review", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    produces = "application/json")
    public ResponseEntity<String> handleReview(@RequestBody MultiValueMap<String, String> review) {
        // System.out.println(review);
        JsonObject jObject = commentService.handleComment(review);
        return createResponseEntity(jObject);
    }

    @PutMapping(path = "/review/{review_id}", consumes = "application/json",
    produces = "application/json")
    public ResponseEntity<String> updateReview(@PathVariable(name = "review_id") String reviewId,
    @RequestBody String jsonUpdate) {
        JsonObject jObject = commentService.updateComment(reviewId, jsonUpdate);
        return createResponseEntity(jObject);
    }

    public ResponseEntity<String> createResponseEntity(JsonObject jObject) {
        if(jObject.containsKey("error")){
            return ResponseEntity.badRequest().body(jObject.toString());
        } else {
            return ResponseEntity.ok().body(jObject.toString());
        }
    }
}
