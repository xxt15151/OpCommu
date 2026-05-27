package com.example.demo.service;

import com.example.demo.model.Group;
import com.example.demo.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public boolean createGroup(Group group) {
        int rows = groupRepository.save(group);
        return rows > 0;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public List<Group> getGroupsByUserId(Long userId) {
        return groupRepository.findByUserId(userId);
    }
}