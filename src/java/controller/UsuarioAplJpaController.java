/*
USUARIO APL JPA CONTROLLER =========================================================
Esta classe serve como agrupamento das operações básicas de CRUD para a classe 
UsuarioApl. Para melhor explicação sobre o que é uma classe JPA CONTROLLER, cf. 
AgendaJpaController, onde isso está explicado.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Vacinacao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.UsuarioApl;

/**
 *
 * @author Pedro
 */
public class UsuarioAplJpaController implements Serializable {

    public UsuarioAplJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioApl usuarioApl) {
        if (usuarioApl.getVacinacaoList() == null) {
            usuarioApl.setVacinacaoList(new ArrayList<Vacinacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Vacinacao> attachedVacinacaoList = new ArrayList<Vacinacao>();
            for (Vacinacao vacinacaoListVacinacaoToAttach : usuarioApl.getVacinacaoList()) {
                vacinacaoListVacinacaoToAttach = em.getReference(vacinacaoListVacinacaoToAttach.getClass(), vacinacaoListVacinacaoToAttach.getCodigo());
                attachedVacinacaoList.add(vacinacaoListVacinacaoToAttach);
            }
            usuarioApl.setVacinacaoList(attachedVacinacaoList);
            em.persist(usuarioApl);
            for (Vacinacao vacinacaoListVacinacao : usuarioApl.getVacinacaoList()) {
                UsuarioApl oldCodigoUsuarioAplOfVacinacaoListVacinacao = vacinacaoListVacinacao.getCodigoUsuarioApl();
                vacinacaoListVacinacao.setCodigoUsuarioApl(usuarioApl);
                vacinacaoListVacinacao = em.merge(vacinacaoListVacinacao);
                if (oldCodigoUsuarioAplOfVacinacaoListVacinacao != null) {
                    oldCodigoUsuarioAplOfVacinacaoListVacinacao.getVacinacaoList().remove(vacinacaoListVacinacao);
                    oldCodigoUsuarioAplOfVacinacaoListVacinacao = em.merge(oldCodigoUsuarioAplOfVacinacaoListVacinacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioApl usuarioApl) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioApl persistentUsuarioApl = em.find(UsuarioApl.class, usuarioApl.getCodigo());
            List<Vacinacao> vacinacaoListOld = persistentUsuarioApl.getVacinacaoList();
            List<Vacinacao> vacinacaoListNew = usuarioApl.getVacinacaoList();
            List<Vacinacao> attachedVacinacaoListNew = new ArrayList<Vacinacao>();
            for (Vacinacao vacinacaoListNewVacinacaoToAttach : vacinacaoListNew) {
                vacinacaoListNewVacinacaoToAttach = em.getReference(vacinacaoListNewVacinacaoToAttach.getClass(), vacinacaoListNewVacinacaoToAttach.getCodigo());
                attachedVacinacaoListNew.add(vacinacaoListNewVacinacaoToAttach);
            }
            vacinacaoListNew = attachedVacinacaoListNew;
            usuarioApl.setVacinacaoList(vacinacaoListNew);
            usuarioApl = em.merge(usuarioApl);
            for (Vacinacao vacinacaoListOldVacinacao : vacinacaoListOld) {
                if (!vacinacaoListNew.contains(vacinacaoListOldVacinacao)) {
                    vacinacaoListOldVacinacao.setCodigoUsuarioApl(null);
                    vacinacaoListOldVacinacao = em.merge(vacinacaoListOldVacinacao);
                }
            }
            for (Vacinacao vacinacaoListNewVacinacao : vacinacaoListNew) {
                if (!vacinacaoListOld.contains(vacinacaoListNewVacinacao)) {
                    UsuarioApl oldCodigoUsuarioAplOfVacinacaoListNewVacinacao = vacinacaoListNewVacinacao.getCodigoUsuarioApl();
                    vacinacaoListNewVacinacao.setCodigoUsuarioApl(usuarioApl);
                    vacinacaoListNewVacinacao = em.merge(vacinacaoListNewVacinacao);
                    if (oldCodigoUsuarioAplOfVacinacaoListNewVacinacao != null && !oldCodigoUsuarioAplOfVacinacaoListNewVacinacao.equals(usuarioApl)) {
                        oldCodigoUsuarioAplOfVacinacaoListNewVacinacao.getVacinacaoList().remove(vacinacaoListNewVacinacao);
                        oldCodigoUsuarioAplOfVacinacaoListNewVacinacao = em.merge(oldCodigoUsuarioAplOfVacinacaoListNewVacinacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarioApl.getCodigo();
                if (findUsuarioApl(id) == null) {
                    throw new NonexistentEntityException("The usuarioApl with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioApl usuarioApl;
            try {
                usuarioApl = em.getReference(UsuarioApl.class, id);
                usuarioApl.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioApl with id " + id + " no longer exists.", enfe);
            }
            List<Vacinacao> vacinacaoList = usuarioApl.getVacinacaoList();
            for (Vacinacao vacinacaoListVacinacao : vacinacaoList) {
                vacinacaoListVacinacao.setCodigoUsuarioApl(null);
                vacinacaoListVacinacao = em.merge(vacinacaoListVacinacao);
            }
            em.remove(usuarioApl);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioApl> findUsuarioAplEntities() {
        return findUsuarioAplEntities(true, -1, -1);
    }

    public List<UsuarioApl> findUsuarioAplEntities(int maxResults, int firstResult) {
        return findUsuarioAplEntities(false, maxResults, firstResult);
    }

    private List<UsuarioApl> findUsuarioAplEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioApl.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public UsuarioApl findUsuarioApl(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioApl.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioAplCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioApl> rt = cq.from(UsuarioApl.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
