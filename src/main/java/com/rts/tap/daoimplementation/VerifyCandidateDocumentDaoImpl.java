package com.rts.tap.daoimplementation;

import org.springframework.stereotype.Repository;
import com.rts.tap.dao.VerifyCandidateDocumentDao;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.CandidateDocument;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class VerifyCandidateDocumentDaoImpl implements VerifyCandidateDocumentDao {

	private EntityManager entityManager;

	public VerifyCandidateDocumentDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public CandidateDocument getDocumentByCandidateId(long candidateId) {
		String hql = "Select c.documents from Candidate c where c.candidateId=:candidateId";
		return (CandidateDocument) (entityManager.createQuery(hql).setParameter("candidateId", candidateId)
				.getSingleResult());
	}

	@Override
	public void updateCandidateDocumentStatus(CandidateDocument candidateDocument) {
		entityManager.merge(candidateDocument);
	}

	@Override
	public void updateCandidateStatus(Long candidateId, String status) {
		Candidate candidate = entityManager.find(Candidate.class, candidateId);
		candidate.setStatus(status);
		entityManager.merge(candidate);
	}
}