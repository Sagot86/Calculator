package server.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import server.model.HistoryUnit;
import server.utils.HibernateSessionFactoryUtil;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class HistoryUnitDAO {

    private org.hibernate.SessionFactory sessionFactory = HibernateSessionFactoryUtil.sessionFactory;

    public HistoryUnitDAO() {
    }

    public void saveMany(List<HistoryUnit> historyUnitList) {
        try (Session currentSession = sessionFactory.openSession()) {
            Transaction transaction = currentSession.beginTransaction();

            for (int i = 0; i < historyUnitList.size(); i++) {
                currentSession.save(historyUnitList.get(i));

                if (i % 20 == 0) {
                    currentSession.flush();
                    currentSession.clear();
                }
            }
            currentSession.flush();
            currentSession.clear();
            transaction.commit();
        }
    }

    /**
     *
     * Можно было бы использовать "FROM HistoryUnit WHERE id IN (select id from HistoryUnit ORDER BY id DESC limit 15) order by id asc", но HQL не поддерживает LIMIT.
     *
     * */
    @SuppressWarnings("unchecked")
    public List<HistoryUnit> getLastFifteen() {
        String select = "FROM HistoryUnit WHERE id between :startIDParam and :lastIDParam ORDER BY id ASC";
        Long lastID = getLastID();
        Long startID = lastID - 15;
        try (Session currentSession = sessionFactory.openSession()){
            Query query = currentSession.createQuery(select);
            query.setParameter("startIDParam", startID);
            query.setParameter("lastIDParam", lastID);
            return query.getResultList();
        }
    }

    public Long getLastID() {
        String selectID = "SELECT id FROM HistoryUnit ORDER BY id DESC";
        try (Session currentSession = sessionFactory.openSession()){
            return (Long)currentSession.createQuery(selectID).setMaxResults(1).getSingleResult();
        } catch (NoResultException exception) {
            return 0L;
        }
    }
}

