package vttp5a_paf.day27ws.repository;

import static vttp5a_paf.day27ws.utils.Constants.MONGO_C_COMMENTS;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepository {
    
    @Autowired
    private MongoTemplate template;

    public void insertComment(Document doc) {
        Document inserted = template.insert(doc, MONGO_C_COMMENTS);
        ObjectId oid = inserted.getObjectId("_id");
        System.out.printf("Object ID >>>> %s", oid.toHexString());
    }
}
