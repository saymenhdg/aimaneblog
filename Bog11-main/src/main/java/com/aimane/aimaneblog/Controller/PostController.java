package com.aimane.aimaneblog.Controller;

import com.aimane.aimaneblog.Model.Post;
import com.aimane.aimaneblog.Model.User;
import com.aimane.aimaneblog.Service.PostService;
import com.aimane.aimaneblog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }




}



