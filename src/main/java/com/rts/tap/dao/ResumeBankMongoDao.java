package com.rts.tap.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.rts.tap.model.ResumeBank;

@Repository
public interface ResumeBankMongoDao extends MongoRepository<ResumeBank, String> {


}
