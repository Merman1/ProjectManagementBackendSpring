package com.example.projectmanagementbackendspring.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TimeService {
    @Autowired
    private TimeRepository timeRepository;

    public List<Time> getAllTimes() {
        return timeRepository.findAll();
    }

    public Optional<Time> getTimeById(Long id) {
        return timeRepository.findById(id);
    }

    public Time saveTime(Time time) {
        return timeRepository.save(time);
    }

    public void deleteTime(Long id) {
        timeRepository.deleteById(id);
    }
}
