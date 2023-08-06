package com.pack.v2.contollers;

import com.pack.v2.models.Post;
import com.pack.v2.models.User;
import com.pack.v2.repositories.PostRepository;
import com.pack.v2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class StarredController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/starred")
    private String starred(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Post> posts = postRepository.findAllByUserIdAndIsStarredTrueAndIsDeletedFalseOrderByCreatedDateDesc(user.getId());
        model.addAttribute("posts", posts);
        return "starred";
    }
    @GetMapping("/starred/note/{id}") // Динамическая страница записи по id
    public String trashNote(@PathVariable(value = "id") long id, Model model, Principal principal) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isEmpty()){
            // Проверка на существование записи и перебрасывание в 404.html
            return "404";
        }
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Post post = postOpt.get();
        if(!post.getUser().equals(user)) {
            return "404";
        }
        if(post.getIsDeleted()){
            return "404";
        }
        List<Post> posts = postRepository.findAllByUserIdAndIsStarredTrueAndIsDeletedFalseOrderByCreatedDateDesc(user.getId());
        model.addAttribute("posts", posts);
        ArrayList<Post> res = new ArrayList<>();
        res.add(post);
        model.addAttribute("post", res);
        return "starred";
    }

    @PostMapping("/note/{id}/starred")
    private String setStarred(@PathVariable(value = "id") long id, Principal principal) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setIsStarred(!post.getIsStarred());
        postRepository.save(post);
        return "redirect:/note/" + post.getId();
    }
}
