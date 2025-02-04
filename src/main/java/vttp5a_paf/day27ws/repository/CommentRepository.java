package vttp5a_paf.day27ws.repository;

import static vttp5a_paf.day27ws.utils.Constants.MONGO_C_COMMENTS;
import static vttp5a_paf.day27ws.utils.Constants.MONGO_F_OID;

import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

@Repository
public class CommentRepository {
    
    @Autowired
    private MongoTemplate template;

    // db.comments.insert({
    //     user: <name form field>,
    //     rating: <rating form field>,
    //     comment: <comment form field>,
    //     ID: <gameId>,
    //     posted: <date>,
    //     name:<The board game's name as per ID>
    // })
    public String insertComment(Document doc) {
        Document inserted = template.insert(doc, MONGO_C_COMMENTS);
        ObjectId oid = inserted.getObjectId(MONGO_F_OID);
        System.out.printf("Object ID >>>> %s", oid.toHexString());

        return oid.toHexString();
    }

    // db.comments.update(
    //     {_id: ObjectId(<oid>)},
    //     {
    //         $push: {"edited", <update>}
    //     }
    // )
    public Long updateComment(String commentId, Document updateEdit) {
        ObjectId oid = new ObjectId(commentId);
        Query query = Query.query(Criteria.where(MONGO_F_OID).is(oid));
        Update updateOps = new Update()
        .push("edited", updateEdit);
        // just push as a Document directly so can cast back to a Document later when taking it out
        
        UpdateResult updateResult = template
        .updateMulti(query, updateOps, Document.class, MONGO_C_COMMENTS);
        
        return updateResult.getModifiedCount();
    }

    // db.games.find({_id: ObjectId(<commentId>)})
    public Optional<Document> getComment(String commentId) {
        ObjectId oid = new ObjectId(commentId);
        Optional<Document> opt = Optional
                .ofNullable(template.findById(oid, Document.class, MONGO_C_COMMENTS));
    
        return opt;
    }
}
