package sos.rest.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;

import sos.rest.models.User;

public class UserService {

    private EntityManagerFactory emf;

    public UserService() {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/SOS");
        properties.put("javax.persistence.jdbc.user", "root");
        properties.put("javax.persistence.jdbc.password", "root");
        properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
        emf = Persistence.createEntityManagerFactory("user-persistance-unit", properties);
    }
    
    public User addUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve una lista filtrada de usuarios
     * @param nameFilter
     * @return Filtred list
     */
    public List<User> getUsers(String nameFilter) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.name LIKE :nameFilter", User.class);
            query.setParameter("nameFilter", "%" + nameFilter + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<User> getAllUsers() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public User getUserById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.find(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User with id " + id + " not found");
            }
            return user;
        } finally {
            em.close();
        }
    }

    public User updateUser(Long id, User updatedUser) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user == null) {
                throw new NotFoundException();
            }
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setAge(updatedUser.getAge());
            em.getTransaction().commit();
            return user;
        } finally {
            em.close();
        }
    }
    

    public User deleteUser(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User with id " + id + " not found");
            }
            em.remove(user);
            em.getTransaction().commit();
            return user;
        } finally {
            em.close();
        }
    }
}
