package vttp5a_paf.day27ws.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static vttp5a_paf.day27ws.utils.Constants.MONGO_C_GAMES;
import static vttp5a_paf.day27ws.utils.Constants.MONGO_F_GID;

import java.util.List;
import java.util.Optional;

@Repository
public class GameRepository {
    
    @Autowired MongoTemplate template;

    // db.games.find({gid: <gameId>})
    public String getGameName(Integer gameId) {

        Criteria criteria = Criteria.where(MONGO_F_GID).is(gameId);
        Query query = Query.query(criteria);

        Optional<List<Document>> opt = Optional.ofNullable(template.find(query, Document.class, MONGO_C_GAMES));

        return opt.map(l -> l.get(0).getString("name")).orElse("null");
    }
}
