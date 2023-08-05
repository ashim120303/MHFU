package com.pack.v2.confs;

import com.pack.v2.models.Post;
import com.pack.v2.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Autowired // Ссылки на репозитории
    private PostRepository postRepository;
    @Scheduled(cron = "0 0 0 * * ?") // Запускается каждый день в полночь
    public void removeOldNotes() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        date = cal.getTime();
        List<Post> posts = postRepository.findAllByIsDeletedTrueAndIsDeletedDateBefore(date);
        for(Post post : posts) {
            postRepository.delete(post);
        }
    }

}

