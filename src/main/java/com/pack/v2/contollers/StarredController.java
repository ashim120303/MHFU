package com.pack.v2.contollers;

import com.pack.v2.models.Post;
import com.pack.v2.models.User;
import com.pack.v2.repositories.PostRepository;
import com.pack.v2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class StarredController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    private static final String UPLOAD_DIR = "./uploads"; // Путь до дериктории с img

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
        return "starredNote";
    }

    @PostMapping("/note/{id}/starred")
    private String setStarred(@PathVariable(value = "id") long id, Principal principal) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setIsStarred(!post.getIsStarred());
        postRepository.save(post);
        return "redirect:/note/" + post.getId();
    }

    @GetMapping("/starred/note/{id}/edit") // Переход на редактирование
    public String edit(@PathVariable(value = "id") long id, Model model, Principal principal) {
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
        ArrayList<Post> res = new ArrayList<>();
        res.add(post);
        model.addAttribute("post", res);
        List<Post> posts = postRepository.findAllByUserIdAndIsStarredTrueAndIsDeletedFalseOrderByCreatedDateDesc(user.getId());
        model.addAttribute("posts", posts);
        return "starredEdit";
    }
    @PostMapping("/starred/note/{id}/edit")
    private String editPost(@RequestParam String title, String text, Long id, Model model,
                            @RequestParam(value = "image", required = false) MultipartFile file,
                            @RequestParam(value = "deleteImage", required = false) String deleteImage, Principal principal) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();
        String fileName = post.getImageName();
        if (file != null && !file.isEmpty()) {
            fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            int count = 0;
            while (Files.exists(filePath)) {
                // Добавляем к имени файла цифру
                count++;
                int dotIndex = fileName.lastIndexOf('.');
                String nameWithoutExtension = fileName.substring(0, dotIndex);
                String extension = fileName.substring(dotIndex);
                fileName = nameWithoutExtension + "(" + count + ")" + extension;
                filePath = uploadPath.resolve(fileName);
            }
            try (InputStream inputStream = file.getInputStream()) {
                filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Could not save file: " + fileName, e);
            }
        } else if (deleteImage != null && deleteImage.equals("true")) {
            fileName = null;
        }
        if ((title != null && !title.isEmpty()) || (text != null && !text.isEmpty()) || (fileName != null && !fileName.isEmpty())) {
            post.setTitle(title);
            post.setText(text);
            post.setImageName(fileName);
            post.setCreatedDate(new Date());
            Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
            if(optionalUser.isPresent()) {
                User user = optionalUser.get();
                post.setUser(user);
                postRepository.save(post);
            }
            return "redirect:/starred/note/{id}";
        }else{
            post = postRepository.findById(id).orElseThrow();
            postRepository.delete(post);
            return "redirect:/";
        }
    }
}
