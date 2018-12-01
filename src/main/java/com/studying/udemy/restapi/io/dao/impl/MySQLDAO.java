package com.studying.udemy.restapi.io.dao.impl;

import com.studying.udemy.restapi.io.dao.DAO;
import com.studying.udemy.restapi.io.entity.UserEntity;
import com.studying.udemy.restapi.shared.dto.UserDTO;
import com.studying.udemy.restapi.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MySQLDAO implements DAO {
    private Session session;

    @Override
    public void openConnection() {
        session = HibernateUtils.getSessionFactory().openSession();
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        UserDTO userDTO = null;
        CriteriaBuilder cb = session.getCriteriaBuilder();

//        Create criteria against a particular persistence class
        CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);

//        Query roots always reference entity
        Root<UserEntity> profileRoot = criteria.from(UserEntity.class);
        criteria.select(profileRoot);
        criteria.where(cb.equal(profileRoot.get("email"), userName));

//        Fetch single result
        Query<UserEntity> query = session.createQuery(criteria);
        List<UserEntity> resultList = query.getResultList();

        if (resultList != null && !resultList.isEmpty()) {
            UserEntity userEntity = resultList.get(0);
            userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDTO);
        }

        return userDTO;
    }

    @Override
    public UserDTO getUser(String id) {
        CriteriaBuilder cb = session.getCriteriaBuilder();

//        Create criteria against a particular persistence class
        CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);

//        Query roots always reference entity
        Root<UserEntity> profileRoot = criteria.from(UserEntity.class);
        criteria.select(profileRoot);
        criteria.where(cb.equal(profileRoot.get("userId"), id));

//        Fetch single result
        UserEntity userEntity = session.createQuery(criteria).getSingleResult();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userEntity, userDTO);

        return userDTO;
    }

    @Override
    public List<UserDTO> getUsers(int start, int limit) {
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);
        Root<UserEntity> userRoot = criteria.from(UserEntity.class);
        criteria.select(userRoot);

        List<UserEntity> searchResult = session.createQuery(criteria)
                .setFirstResult(start)
                .setMaxResults(limit)
                .getResultList();

        return searchResult.stream().map(this::convertUserEntityToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO saveUser(UserDTO user) {
        UserDTO returnValue = null;
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        session.beginTransaction();
        session.save(userEntity);
        session.getTransaction().commit();

        returnValue = new UserDTO();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public void updateUser(UserDTO user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        session.beginTransaction();
        session.update(userEntity);
        session.getTransaction().commit();
    }

    @Override
    public void closeConnection() {
        if (session != null) {
            session.close();
        }
    }

    private UserDTO convertUserEntityToDto(UserEntity entity) {
        UserDTO returnValue = new UserDTO();
        BeanUtils.copyProperties(entity, returnValue);

        return returnValue;
    }
}
