package com.pack.v2.contollers;

import com.pack.v2.models.Post;
import com.pack.v2.models.User;
import com.pack.v2.repositories.PostRepository;
import com.pack.v2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class TrashController {
    @Autowired // Ссылки на репозитории
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/trash") // Вывод корзины
    private String trash(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Post> posts = postRepository.findAllByUserIdAndIsDeletedTrueOrderByIsDeletedDateDesc(user.getId());
        model.addAttribute("posts", posts);
        return "trash";
    }
    @PostMapping("/note/{id}/remove")
    private String removePost(@PathVariable(value = "id") long id, Principal principal){
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
        post.setIsDeleted(true);
        post.setIsDeletedDate(new Date());
        postRepository.save(post);
        return "redirect:/";
    }
    @GetMapping("/trash/note/{id}") // Динамическая страница записи по id
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
        List<Post> posts = postRepository.findAllByUserIdAndIsDeletedTrueOrderByIsDeletedDateDesc(user.getId());
        model.addAttribute("posts", posts);
        ArrayList<Post> res = new ArrayList<>();
        res.add(post);
        model.addAttribute("post", res);
        return "trash";
    }
    @PostMapping("/note/{id}/restore")
    private String restorePost(@PathVariable(value = "id") long id, Principal principal){
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
        post.setIsDeleted(false);
        post.setIsDeletedDate(null);
        postRepository.save(post);
        return "redirect:/";
    }
    @PostMapping("/note/{id}/removeForever")
    private String removeForever(@PathVariable(value = "id") long id, Principal principal){
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
        postRepository.delete(post);
        return "redirect:/trash";
    }
}