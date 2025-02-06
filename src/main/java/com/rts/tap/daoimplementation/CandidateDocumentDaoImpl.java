package com.rts.tap.daoimplementation;

import org.springframework.stereotype.Repository;

import com.rts.tap.dao.CandidateDocumentDao;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.CandidateDocument;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class CandidateDocumentDaoImpl implements CandidateDocumentDao {

	private EntityManager entityManager;

	public CandidateDocumentDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Candidate findById(Long id) {
		return entityManager.find(Candidate.class, id);
	}

	@Override
	public void update(Candidate candidate) {
		entityManager.merge(candidate);
	}

	@Override

	public CandidateDocument saveDocuments(CandidateDocument candidateDocument) {
		entityManager.persist(candidateDocument);
		return candidateDocument;
	}

	@Override
	public CandidateDocument updateCandidateDocuments(CandidateDocument candidateDocument) {
		entityManager.merge(candidateDocument);
		return candidateDocument;
	}

	@Override
	public CandidateDocument getCandidateDocumentByCandidateId(Long candidateId) {
		String jpql = "SELECT cd.documents FROM Candidate cd WHERE cd.candidateId = :candidateId";
		TypedQuery<CandidateDocument> query = entityManager.createQuery(jpql, CandidateDocument.class);
		query.setParameter("candidateId", candidateId);
		return query.getSingleResult();
	}
}
