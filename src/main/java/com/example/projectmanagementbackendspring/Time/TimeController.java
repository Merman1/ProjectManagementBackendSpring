package com.example.projectmanagementbackendspring.Time;

import com.example.projectmanagementbackendspring.Sprint.Sprint;
import com.example.projectmanagementbackendspring.Sprint.SprintService;
import com.example.projectmanagementbackendspring.user.User;
import com.example.projectmanagementbackendspring.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/time")
public class TimeController {
    @Autowired
    private TimeService timeService;

    @Autowired
    private UserService userService;
    @Autowired
    private SprintService sprintService;
    @GetMapping
    public ResponseEntity<List<Time>> getAllTimes() {
        List<Time> times = timeService.getAllTimes();
        return new ResponseEntity<>(times, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Time> getTimeById(@PathVariable Long id) {
        Optional<Time> time = timeService.getTimeById(id);
        return time.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Time> addTime(@RequestBody Time time) {
        if (time.getUser() == null || time.getSprint() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Pobierz id użytkownika z obiektu User
        Long userId = time.getUser().getId();
        // Pobierz id sprintu
        Long sprintId = time.getSprint().getId();

        // Pobierz pełne obiekty User i Sprint z bazy danych
        User user = userService.findById(userId);

        Sprint sprint = sprintService.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        // Ustaw obiekty User i Sprint w obiekcie time
        time.setUser(user);
        time.setSprint(sprint);

        // Zapisz obiekt time
        Time savedTime = timeService.saveTime(time);
        return new ResponseEntity<>(savedTime, HttpStatus.CREATED);
    }






    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        timeService.deleteTime(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
