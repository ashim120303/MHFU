package com.pack.v2.contollers;

import com.pack.v2.models.Post;
import com.pack.v2.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired // Ссылка на репозиторий
    private PostRepository postRepository;
    private static final String UPLOAD_DIR = "./uploads"; // Путь до дериктории с img

    @GetMapping("/") // Вывод главной
    private String home(Model model) {
        Iterable <Post> posts = postRepository.findAllByOrderByCreatedDateDesc();
        model.addAttribute("posts", posts);

        //Удаление ненужных картин
        // Получаем список всех файлов в директории uploads
        File uploadDir = new File(UPLOAD_DIR);
        File[] files = uploadDir.listFiles();
        // Получаем список имен файлов из базы данных
        List<String> imageNames = new ArrayList<>();
        Iterable<Post> posts1 = postRepository.findAll();
        for (Post post : posts1) {
            String imageName = post.getImageName();
            if (imageName != null) {
                imageNames.add(imageName);
            }
        }
        // Удаляем файлы, имена которых отсутствуют в базе данных
        for (File file : files) {
            String fileName = file.getName();
            if (!imageNames.contains(fileName)) {
                file.delete();
            }
        }
        return "index";
    }
    @GetMapping("/note/{id}") // Динамическая страница записи по id
    public String note(@PathVariable(value = "id") long id, Model model) {
        if(!postRepository.existsById(id)){
            // Проверка на существование записи и перебрасывание в 404.html
            return "404";
        }
        Iterable <Post> posts = postRepository.findAllByOrderByCreatedDateDesc(); // Вывод всех ссылок на заметки
        model.addAttribute("posts", posts);

        Optional<Post> post = postRepository.findById(id); // Вывод заметки по id
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);

        return "note";
    }

    @PostMapping("/") // Сохранение заголовка и текста
    private String addPost(@RequestParam String title, Long id, String text, String imageName, Model model,
                           @RequestParam(value = "image", required = false) MultipartFile file) throws IOException{
        String fileName = null;
        if(file != null && !file.isEmpty()) {
            fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Проверяем, существует ли файл с таким же именем
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
        }

        Post post = new Post();
        if ((title != null && !title.isEmpty()) || (text != null && !text.isEmpty()) || (fileName != null && !fileName.isEmpty())) {
            post.setTitle(title);
            post.setText(text);
            post.setImageName(fileName);
            post.setCreatedDate(new Date());
            postRepository.save(post);
            return "redirect:/note/" + post.getId();
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/note/{id}/edit") // Переход на редактирование
    public String edit(@PathVariable(value = "id") long id, Model model) {
        if(!postRepository.existsById(id)){
            // Проверка на существование записи и перебрасывание в 404.html
            return "404";
        }
        Optional<Post> post = postRepository.findById(id); // Вывод заметки по id
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);

        Iterable <Post> posts = postRepository.findAllByOrderByCreatedDateDesc(); // Вывод всех ссылок на заметки
        model.addAttribute("posts", posts);
        return "edit";
    }

    @PostMapping("/note/{id}/edit")
    private String editPost(@RequestParam String title, String text, Long id, Model model,
                            @RequestParam(value = "image", required = false) MultipartFile file,
                            @RequestParam(value = "deleteImage", required = false) String deleteImage) throws IOException {
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
            postRepository.save(post);
            return "redirect:/note/{id}";
        }else{
            post = postRepository.findById(id).orElseThrow();
            postRepository.delete(post);
            return "redirect:/";
        }
    }
    @PostMapping("/note/{id}/remove")
    private String removePost(@PathVariable(value = "id") long id, Model model){
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/";
    }
}
